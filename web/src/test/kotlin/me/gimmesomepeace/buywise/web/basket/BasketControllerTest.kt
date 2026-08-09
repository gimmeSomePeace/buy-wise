package me.gimmesomepeace.buywise.web.basket

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.basket.BasketQuery
import me.gimmesomepeace.buywise.application.basket.add.AddProductToBasketUseCase
import me.gimmesomepeace.buywise.application.basket.change.ChangeBasketItemQuantityUseCase
import me.gimmesomepeace.buywise.application.basket.clear.ClearBasketUseCase
import me.gimmesomepeace.buywise.application.basket.remove.RemoveFromBasketUseCase
import me.gimmesomepeace.buywise.domain.basket.BasketException
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.qty
import me.gimmesomepeace.buywise.web.basket.add.AddProductToBasketRequest
import me.gimmesomepeace.buywise.web.basket.change.ChangeProductQuantityRequest
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
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.jackson.databind.ObjectMapper

@WebMvcTest(BasketController::class)
class BasketControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var basketQuery: BasketQuery

    @MockkBean(relaxed = true)
    lateinit var changeBasketItemQuantityUseCase: ChangeBasketItemQuantityUseCase

    @MockkBean(relaxed = true)
    lateinit var addProductToBasketUseCase: AddProductToBasketUseCase

    @MockkBean(relaxed = true)
    lateinit var clearBasketUseCase: ClearBasketUseCase

    @MockkBean(relaxed = true)
    lateinit var removeFromBasketUseCase: RemoveFromBasketUseCase

    @Nested
    inner class Get {
        @Test
        fun `should return basket`() =
            runTest {
                coEvery { basketQuery.find() } returns
                    basketDetails(
                        basketItemsDetails(),
                        basketItemsDetails(),
                    )

                val mvcResult =
                    mockMvc
                        .get("/basket") {
                            accept = MediaType.APPLICATION_JSON
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.items").isArray)
                    .andExpect(jsonPath("$.items.length()").value(2))
            }

        @Test
        fun `should return empty basket when not found`() =
            runTest {
                coEvery { basketQuery.find() } returns null

                val mvcResult =
                    mockMvc
                        .get("/basket") {
                            accept = MediaType.APPLICATION_JSON
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.items").isArray)
                    .andExpect(jsonPath("$.items.length()").value(0))
            }
    }

    @Nested
    inner class Add {
        @Test
        fun `should add product to empty basket`() =
            runTest {
                val productId = productId()

                val mvcResult =
                    mockMvc
                        .post("/basket/items") {
                            contentType = MediaType.APPLICATION_JSON
                            content =
                                objectMapper.writeValueAsString(
                                    AddProductToBasketRequest(productId.value, 5),
                                )
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isNoContent)

                coVerify(exactly = 1) {
                    addProductToBasketUseCase.execute(productId, 5.qty())
                }
            }

        @ParameterizedTest
        @ValueSource(ints = [-1, 0])
        fun `should fail when quantity is not positive`(quantity: Int) =
            runTest {
                mockMvc
                    .post("/basket/items") {
                        contentType = MediaType.APPLICATION_JSON
                        content =
                            objectMapper.writeValueAsString(
                                AddProductToBasketRequest(productId().value, quantity),
                            )
                    }.andExpect {
                        status { isBadRequest() }
                    }
            }
    }

    @Nested
    inner class ChangeProductQuantity {
        @Test
        fun `should change product quantity`() =
            runTest {
                val productId = productId()

                val mvcResult =
                    mockMvc
                        .put("/basket/items/${productId.value}") {
                            contentType = MediaType.APPLICATION_JSON
                            content =
                                objectMapper.writeValueAsString(
                                    ChangeProductQuantityRequest(10),
                                )
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isNoContent)

                coVerify(exactly = 1) {
                    changeBasketItemQuantityUseCase.execute(productId, 10.qty())
                }
            }

        @Test
        fun `should fail when quantity is negative`() =
            runTest {
                mockMvc
                    .put("/basket/items/${productId().value}") {
                        contentType = MediaType.APPLICATION_JSON
                        content =
                            objectMapper.writeValueAsString(
                                ChangeProductQuantityRequest(-1),
                            )
                    }.andExpect {
                        status { isBadRequest() }
                    }
            }
    }

    @Nested
    inner class Remove {
        @Test
        fun `should remove product from basket`() =
            runTest {
                val productId = productId()
                val mvcResult =
                    mockMvc
                        .delete("/basket/items/${productId.value}")
                        .andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isNoContent)

                coVerify(exactly = 1) {
                    removeFromBasketUseCase.execute(productId)
                }
            }

        @Test
        fun `should fail when product is not in basket`() =
            runTest {
                val productId = productId()

                coEvery {
                    removeFromBasketUseCase.execute(productId)
                } throws BasketException.ProductNotInBasket(productId)

                val mvcResult = mockMvc.delete("/basket/items/${productId.value}").andReturn()
                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isNotFound)
            }
    }

    @Nested
    inner class Clear {
        @Test
        fun `should clear basket`() {
            val mvcResult = mockMvc.delete("/basket/items").andReturn()
            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isNoContent)

            coVerify(exactly = 1) {
                clearBasketUseCase.execute()
            }
        }
    }
}
