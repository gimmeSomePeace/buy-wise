package me.gimmesomepeace.buywise.web.product

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.product.ProductQuery
import me.gimmesomepeace.buywise.application.product.create.CreateProductUseCase
import me.gimmesomepeace.buywise.application.product.delete.DeleteProductUseCase
import me.gimmesomepeace.buywise.application.product.list.ListProductsUseCase
import me.gimmesomepeace.buywise.application.product.rename.RenameProductUseCase
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.shared.cursor
import me.gimmesomepeace.buywise.domain.product.ProductException
import me.gimmesomepeace.buywise.domain.product.product
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.web.product.rename.RenameProductRequest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.jackson.databind.ObjectMapper

@WebMvcTest(ProductController::class)
class ProductControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @MockkBean
    lateinit var listProductsUseCase: ListProductsUseCase

    @MockkBean
    lateinit var createProductUseCase: CreateProductUseCase

    @MockkBean
    lateinit var deleteProductUseCase: DeleteProductUseCase

    @MockkBean
    lateinit var renameProductUseCase: RenameProductUseCase

    @MockkBean
    lateinit var productQuery: ProductQuery

    @Nested
    inner class Get {
        @Test
        fun `should return existing product`() {
            val productId = productId()
            coEvery { productQuery.find(productId) } returns
                product(id = productId)

            val mvcResult =
                mockMvc
                    .get("/products/${productId.value}")
                    .andReturn()

            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productId.value.toString()))
        }

        @Test
        fun `should return 404 when product not found`() {
            val productId = productId()
            coEvery { productQuery.find(productId) } throws
                ProductException.NotFound(productId)

            val mvcResult =
                mockMvc
                    .get("/products/${productId.value}")
                    .andReturn()

            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound)
        }
    }

    @Nested
    inner class Create {
        @Test
        fun `should create new product`() {
            val productId = productId()
            coEvery { createProductUseCase.execute(any()) } returns
                product(id = productId)

            val mvcResult =
                mockMvc
                    .post("/products") {
                        contentType = MediaType.APPLICATION_JSON
                        content =
                            mapper.writeValueAsString(createProductRequest())
                    }.andReturn()

            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id").value(productId.value.toString()))
                .andExpect(
                    header().string("Location", "/products/${productId.value}"),
                )
        }

        @Test
        fun `should fail when request is invalid`() {
            mockMvc
                .post("/products") {
                    contentType = MediaType.APPLICATION_JSON
                    content =
                        mapper.writeValueAsString(
                            createProductRequest(name = "   "),
                        )
                }.andExpect {
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `should delete product`() =
            runTest {
                val productId = productId()
                coJustRun { deleteProductUseCase.execute(productId) }

                val mvcResult =
                    mockMvc
                        .delete("/products/${productId.value}")
                        .andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isNoContent)

                coVerify(exactly = 1) {
                    deleteProductUseCase.execute(productId)
                }
            }

        @Test
        fun `should return 404 when product is not found`() =
            runTest {
                val productId = productId()
                coEvery {
                    deleteProductUseCase.execute(productId)
                } throws ProductException.NotFound(productId)

                val mvcResult =
                    mockMvc
                        .delete("/products/${productId.value}")
                        .andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isNotFound)
            }
    }

    @Nested
    inner class Rename {
        @Test
        fun `should rename product`() =
            runTest {
                val productId = productId()

                coJustRun {
                    renameProductUseCase.execute(productId, "NEW NAME")
                }

                val mvcResult =
                    mockMvc
                        .patch("/products/${productId.value}") {
                            contentType = MediaType.APPLICATION_JSON
                            content =
                                mapper.writeValueAsString(
                                    RenameProductRequest("NEW NAME"),
                                )
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isNoContent)

                coVerify(exactly = 1) {
                    renameProductUseCase.execute(productId, "NEW NAME")
                }
            }
    }

    @Nested
    inner class List {
        @Test
        fun `should return first page`() =
            runTest {
                val request =
                    PageRequest(
                        pageSize = 20,
                        cursor = null,
                    )

                val page =
                    Page(
                        items = listOf(product()),
                        cursor = cursor("next-page"),
                    )

                coEvery {
                    listProductsUseCase.execute(request)
                } returns page

                val mvcResult = mockMvc.get("/products").andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk)
                    .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON),
                    ).andExpect(jsonPath("$.products").isArray)
                    .andExpect(jsonPath("$.products.length()").value(1))
                    .andExpect(jsonPath("$.nextPageToken").value("next-page"))
            }

        @Test
        fun `should pass page request`() =
            runTest {
                val request =
                    PageRequest(
                        pageSize = 5,
                        cursor = cursor("abc"),
                    )

                coEvery {
                    listProductsUseCase.execute(request)
                } returns
                    Page(
                        items = emptyList(),
                        cursor = null,
                    )

                val mvcResult =
                    mockMvc
                        .get("/products") {
                            param("page_size", "5")
                            param("page_token", "abc")
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk)

                coVerify(exactly = 1) {
                    listProductsUseCase.execute(request)
                }
            }

        @ParameterizedTest
        @ValueSource(ints = [-1, 0])
        fun `should fail when page size is not positive`(
            pageSize: Int,
        ) = runTest {
            val mvcResult =
                mockMvc
                    .get("/products") {
                        param("page_size", pageSize.toString())
                    }.andReturn()

            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest)
        }
    }
}
