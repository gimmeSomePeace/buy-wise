package me.gimmesomepeace.buywise.web.product

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.product.create.CreateProductUseCase
import me.gimmesomepeace.buywise.application.product.delete.DeleteProductUseCase
import me.gimmesomepeace.buywise.application.product.get.GetProductUseCase
import me.gimmesomepeace.buywise.application.product.list.ListProductsUseCase
import me.gimmesomepeace.buywise.application.product.productDetails
import me.gimmesomepeace.buywise.application.product.productListItem
import me.gimmesomepeace.buywise.application.product.rename.RenameProductUseCase
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.shared.cursor
import me.gimmesomepeace.buywise.domain.product.ProductException
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.user.userId
import me.gimmesomepeace.buywise.web.TestSecurityConfig
import me.gimmesomepeace.buywise.web.authenticatedAs
import me.gimmesomepeace.buywise.web.product.rename.RenameProductRequest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
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
@Import(TestSecurityConfig::class)
class ProductControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @MockkBean
    lateinit var getProductUseCase: GetProductUseCase

    @MockkBean
    lateinit var listProductsUseCase: ListProductsUseCase

    @MockkBean
    lateinit var createProductUseCase: CreateProductUseCase

    @MockkBean
    lateinit var deleteProductUseCase: DeleteProductUseCase

    @MockkBean
    lateinit var renameProductUseCase: RenameProductUseCase

    @Nested
    inner class Get {
        @Test
        fun `should return existing product`() {
            val userId = userId()
            val productId = productId()
            coEvery {
                getProductUseCase.execute(userId, productId)
            } returns productDetails(id = productId)

            val mvcResult =
                mockMvc
                    .get("/products/${productId.value}") {
                        with(authenticatedAs(userId))
                    }.andReturn()

            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productId.value.toString()))
        }

        @Test
        fun `should return 400 when productId is not valid UUID`() {
            val userId = userId()
            mockMvc
                .get("/products/not-valid-UUID") {
                    with(authenticatedAs(userId))
                }.andExpect {
                    status { isBadRequest() }
                }
        }

        @Test
        fun `should return 404 when product not found`() {
            val userId = userId()
            val productId = productId()
            coEvery { getProductUseCase.execute(userId, productId) } throws
                ProductException.NotFound(productId)

            val mvcResult =
                mockMvc
                    .get("/products/${productId.value}") {
                        with(authenticatedAs(userId))
                    }.andReturn()

            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound)
        }
    }

    @Nested
    inner class Create {
        @Test
        fun `should create new product`() {
            val userId = userId()
            val productId = productId()

            coEvery {
                createProductUseCase.execute(
                    ownerId = userId,
                    any(),
                )
            } returns
                productDetails(id = productId, ownerId = userId)

            val mvcResult =
                mockMvc
                    .post("/products") {
                        contentType = MediaType.APPLICATION_JSON
                        content =
                            mapper.writeValueAsString(createProductRequest())
                        with(authenticatedAs(userId))
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
            val userId = userId()
            mockMvc
                .post("/products") {
                    contentType = MediaType.APPLICATION_JSON
                    content =
                        mapper.writeValueAsString(
                            createProductRequest(name = "   "),
                        )
                    with(authenticatedAs(userId))
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
                val userId = userId()
                val productId = productId()
                coJustRun { deleteProductUseCase.execute(userId, productId) }

                val mvcResult =
                    mockMvc
                        .delete("/products/${productId.value}") {
                            with(authenticatedAs(userId))
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isNoContent)

                coVerify(exactly = 1) {
                    deleteProductUseCase.execute(userId, productId)
                }
            }

        @Test
        fun `should return 400 when productId is not valid UUID`() {
            val userId = userId()
            mockMvc
                .delete("/products/not-valid-UUID") {
                    with(authenticatedAs(userId))
                }.andExpect {
                    status { isBadRequest() }
                }
        }

        @Test
        fun `should return 404 when product is not found`() =
            runTest {
                val userId = userId()
                val productId = productId()
                coEvery {
                    deleteProductUseCase.execute(userId, productId)
                } throws ProductException.NotFound(productId)

                val mvcResult =
                    mockMvc
                        .delete("/products/${productId.value}") {
                            with(authenticatedAs(userId))
                        }.andReturn()

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
                val userId = userId()
                val productId = productId()

                coJustRun {
                    renameProductUseCase.execute(userId, productId, "NEW NAME")
                }

                val request = RenameProductRequest("NEW NAME")
                val mvcResult =
                    mockMvc
                        .patch("/products/${productId.value}") {
                            contentType = MediaType.APPLICATION_JSON
                            content = mapper.writeValueAsString(request)
                            with(authenticatedAs(userId))
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isNoContent)

                coVerify(exactly = 1) {
                    renameProductUseCase.execute(userId, productId, "NEW NAME")
                }
            }

        @Test
        fun `should return 400 when productId is not valid UUID`() {
            val userId = userId()
            mockMvc
                .patch("/products/not-valid-UUID") {
                    contentType = MediaType.APPLICATION_JSON
                    content = mapper.writeValueAsString(renameProductRequest())
                    with(authenticatedAs(userId))
                }.andExpect {
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    inner class List {
        @Test
        fun `should return first page`() =
            runTest {
                val ownerId = userId()
                val request =
                    PageRequest(
                        pageSize = 20,
                        cursor = null,
                    )

                val page =
                    Page(
                        items = listOf(productListItem()),
                        cursor = cursor("next-page"),
                    )

                coEvery {
                    listProductsUseCase.execute(
                        ownerId = ownerId,
                        request = request,
                    )
                } returns page

                val mvcResult =
                    mockMvc
                        .get("/products") {
                            with(authenticatedAs(ownerId))
                        }.andReturn()

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
                val userId = userId()
                val request =
                    PageRequest(
                        pageSize = 5,
                        cursor = cursor("abc"),
                    )

                coEvery {
                    listProductsUseCase.execute(ownerId = userId, request)
                } returns
                    Page(
                        items = emptyList(),
                        cursor = null,
                    )

                val mvcResult =
                    mockMvc
                        .get("/products") {
                            with(authenticatedAs(userId))
                            param("page_size", "5")
                            param("page_token", "abc")
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk)

                coVerify(exactly = 1) {
                    listProductsUseCase.execute(userId, request)
                }
            }

        @ParameterizedTest
        @ValueSource(ints = [-1, 0])
        fun `should return 400 when page size is not positive`(pageSize: Int) =
            runTest {
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
