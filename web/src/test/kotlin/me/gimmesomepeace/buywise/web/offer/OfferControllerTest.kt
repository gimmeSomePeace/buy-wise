package me.gimmesomepeace.buywise.web.offer

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.offer.create.CreateOfferUseCase
import me.gimmesomepeace.buywise.application.offer.delete.DeleteOfferUseCase
import me.gimmesomepeace.buywise.application.offer.get.GetOfferUseCase
import me.gimmesomepeace.buywise.application.offer.list.ListOffersUseCase
import me.gimmesomepeace.buywise.application.offer.offerDetails
import me.gimmesomepeace.buywise.application.offer.offerListItem
import me.gimmesomepeace.buywise.application.offer.price.change.ChangeOfferPriceUseCase
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.shared.cursor
import me.gimmesomepeace.buywise.domain.offer.OfferException
import me.gimmesomepeace.buywise.domain.offer.offerId
import me.gimmesomepeace.buywise.domain.shared.Currency
import me.gimmesomepeace.buywise.domain.shared.usd
import me.gimmesomepeace.buywise.domain.user.userId
import me.gimmesomepeace.buywise.web.TestSecurityConfig
import me.gimmesomepeace.buywise.web.authenticatedAs
import me.gimmesomepeace.buywise.web.offer.update.ChangePriceRequest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import tools.jackson.databind.ObjectMapper

@WebMvcTest(OfferController::class)
@Import(TestSecurityConfig::class)
class OfferControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @MockkBean
    lateinit var getOfferUseCase: GetOfferUseCase

    @MockkBean
    lateinit var createOfferUseCase: CreateOfferUseCase

    @MockkBean
    lateinit var changeOfferPriceUseCase: ChangeOfferPriceUseCase

    @MockkBean
    lateinit var deleteOfferUseCase: DeleteOfferUseCase

    @MockkBean
    lateinit var listOffersUseCase: ListOffersUseCase

    @Nested
    inner class Get {
        @Test
        fun `should return offer`() {
            val offerId = offerId()
            val userId = userId()
            coEvery { getOfferUseCase.execute(userId, offerId) } returns offerDetails(id = offerId)

            val mvcResult =
                mockMvc
                    .get("/offers/${offerId.value}") {
                        contentType = MediaType.APPLICATION_JSON
                        with(authenticatedAs(userId))
                    }.andReturn()

            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(offerId.value.toString()))
        }

        @Test
        fun `should return 400 when offer id is not valid UUID`() {
            val userId = userId()
            mockMvc.get("/offers/not-valid-uuid") {
                with(authenticatedAs(userId))
            }.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `should return 404 when not found`() {
            val userId = userId()
            val offerId = offerId()
            coEvery { getOfferUseCase.execute(userId, offerId) } throws OfferException.NotFound(offerId)

            val mvcResult =
                mockMvc
                    .get("/offers/${offerId.value}") {
                        contentType = MediaType.APPLICATION_JSON
                        with(authenticatedAs(userId))
                    }.andReturn()

            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound)
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class Create {
        @Test
        fun `should create offer`() {
            val userId = userId()
            val offerId = offerId()
            coEvery {
                createOfferUseCase.execute(any(), any(), any())
            } returns offerDetails(id = offerId)

            val request = createOfferRequest(unitPrice = 5.toBigDecimal(), currency = Currency.USD)

            val mvcResult =
                mockMvc
                    .post("/offers") {
                        contentType = MediaType.APPLICATION_JSON
                        content = mapper.writeValueAsString(request)
                        with(authenticatedAs(userId))
                    }.andReturn()

            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id").value(offerId.value.toString()))
                .andExpect(
                    header().string("Location", "/offers/${offerId.value}"),
                )

            coVerify {
                createOfferUseCase.execute(any(), any(), 5.usd())
            }
        }

        @ParameterizedTest
        @MethodSource("me.gimmesomepeace.buywise.web.offer.OfferTestData#invalidCreateOfferRequests")
        fun `should fail when request is not valid`(
            requestBody: Map<String, Any?>
        ) {
            mockMvc
                .post("/offers") {
                    contentType = MediaType.APPLICATION_JSON
                    content = mapper.writeValueAsString(requestBody)
                    with(authenticatedAs(userId()))
                }.andExpect {
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    inner class ChangePrice {
        @Test
        fun `should change price`() {
            val userId = userId()
            val offerId = offerId()
            coEvery { changeOfferPriceUseCase.execute(userId, offerId, 1.usd()) } just Runs

            val request =
                ChangePriceRequest(
                    newPrice = 1.toBigDecimal(),
                    newCurrency = Currency.USD,
                )
            val mvcResult =
                mockMvc
                    .patch("/offers/${offerId.value}") {
                        contentType = MediaType.APPLICATION_JSON
                        content = mapper.writeValueAsString(request)
                        with(authenticatedAs(userId))
                    }.andReturn()

            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isNoContent)

            coVerify {
                changeOfferPriceUseCase.execute(userId, offerId, 1.usd())
            }
        }

        @ParameterizedTest
        @ValueSource(ints = [-1, 0])
        fun `should fail when unit price is not positive`(
            unitPrice: Int,
        ) {
            val userId = userId()
            val request =
                ChangePriceRequest(
                    newPrice = unitPrice.toBigDecimal(),
                    newCurrency = Currency.USD,
                )
            mockMvc
                .patch("/offers/${offerId().value}") {
                    contentType = MediaType.APPLICATION_JSON
                    content = mapper.writeValueAsString(request)
                    with(authenticatedAs(userId))
                }.andExpect {
                    status { isBadRequest() }
                }
        }

        @Test
        fun `should return 404 when offer does not exist`() {
            val userId = userId()
            val offerId = offerId()

            coEvery {
                changeOfferPriceUseCase.execute(userId, offerId, 1.usd())
            } throws OfferException.NotFound(offerId)

            val request =
                ChangePriceRequest(
                    newPrice = 1.toBigDecimal(),
                    newCurrency = Currency.USD,
                )
            val mvcResult =
                mockMvc
                    .patch("/offers/${offerId.value}") {
                        contentType = MediaType.APPLICATION_JSON
                        content = mapper.writeValueAsString(request)
                        with(authenticatedAs(userId))
                    }.andReturn()

            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound)
        }

        @Test
        fun `should return 400 when offerId is not valid UUID`() {
            val userId = userId()
            mockMvc.patch("/offers/not-valid-uuid") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(changePriceRequest())
                with(authenticatedAs(userId))
            }.andExpect {
                status { isBadRequest() }
            }
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `should delete offer`() {
            val userId = userId()
            val offerId = offerId()
            coEvery { deleteOfferUseCase.execute(userId, offerId) } just Runs

            val mvcResult =
                mockMvc
                    .delete("/offers/${offerId.value}") {
                        with(authenticatedAs(userId))
                    }.andReturn()
            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isNoContent)

            coVerify(exactly = 1) {
                deleteOfferUseCase.execute(userId, offerId)
            }
        }

        @Test
        fun `should return 400 when offerId is not valid UUID`() {
            mockMvc.delete("/offers/not-valid-uuid") {
                with(authenticatedAs(userId()))
            }.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `should return 404 when offer does not exist`() {
            val userId = userId()
            val offerId = offerId()
            coEvery { deleteOfferUseCase.execute(userId, offerId) } throws
                OfferException.NotFound(offerId)

            val mvcResult =
                mockMvc
                    .delete("/offers/${offerId.value}") {
                        with(authenticatedAs(userId))
                    }.andReturn()
            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound)
        }
    }

    @Nested
    inner class List {
        @Test
        fun `should return first page`() =
            runTest {
                val userId = userId()
                val request =
                    PageRequest(
                        pageSize = 20,
                        cursor = null,
                    )

                val page =
                    Page(
                        items = listOf(offerListItem()),
                        cursor = cursor("next-page"),
                    )

                coEvery {
                    listOffersUseCase.execute(userId = userId, request)
                } returns page

                val mvcResult = mockMvc.get("/offers") {
                    with(authenticatedAs(userId))
                }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk)
                    .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON),
                    ).andExpect(jsonPath("$.offers").isArray)
                    .andExpect(jsonPath("$.offers.length()").value(1))
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
                    listOffersUseCase.execute(userId = userId, request)
                } returns
                    Page(
                        items = emptyList(),
                        cursor = null,
                    )

                val mvcResult =
                    mockMvc
                        .get("/offers") {
                            param("page_size", "5")
                            param("page_token", "abc")
                            with(authenticatedAs(userId))
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk)

                coVerify(exactly = 1) {
                    listOffersUseCase.execute(userId = userId, request)
                }
            }

        @ParameterizedTest
        @ValueSource(ints = [-1, 0])
        fun `should fail when page size is not positive`(
            pageSize: Int,
        ) = runTest {
            val userId = userId()
            val mvcResult =
                mockMvc
                    .get("/offers") {
                        param("page_size", pageSize.toString())
                        with(authenticatedAs(userId))
                    }.andReturn()

            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest)
        }
    }
}
