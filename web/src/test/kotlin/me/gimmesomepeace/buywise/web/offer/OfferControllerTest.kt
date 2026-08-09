package me.gimmesomepeace.buywise.web.offer

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.offer.OfferQuery
import me.gimmesomepeace.buywise.application.offer.create.CreateOfferUseCase
import me.gimmesomepeace.buywise.application.offer.delete.DeleteOfferUseCase
import me.gimmesomepeace.buywise.application.offer.list.ListOffersUseCase
import me.gimmesomepeace.buywise.application.offer.price.change.ChangePriceUseCase
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.shared.cursor
import me.gimmesomepeace.buywise.domain.offer.OfferException
import me.gimmesomepeace.buywise.domain.offer.offer
import me.gimmesomepeace.buywise.domain.offer.offerId
import me.gimmesomepeace.buywise.domain.shared.Currency
import me.gimmesomepeace.buywise.domain.shared.usd
import me.gimmesomepeace.buywise.web.offer.update.ChangePriceRequest
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

@WebMvcTest(OfferController::class)
class OfferControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @MockkBean
    lateinit var offerQuery: OfferQuery
    @MockkBean
    lateinit var createOfferUseCase: CreateOfferUseCase
    @MockkBean
    lateinit var changePriceUseCase: ChangePriceUseCase
    @MockkBean
    lateinit var deleteOfferUseCase: DeleteOfferUseCase
    @MockkBean
    lateinit var listOffersUseCase: ListOffersUseCase

    @Nested
    inner class Get {
        @Test
        fun `should return offer`() {
            val offerId = offerId()
            coEvery { offerQuery.find(offerId) } returns offer(offerId)

            val mvcResult = mockMvc.get("/offers/${offerId.value}") {
                contentType = MediaType.APPLICATION_JSON
            }.andReturn()

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(offerId.value.toString()))
        }

        @Test
        fun `should return 404 when not found`() {
            coEvery { offerQuery.find(any()) } returns null

            val mvcResult = mockMvc.get("/offers/${offerId().value}") {
                contentType = MediaType.APPLICATION_JSON
            }.andReturn()

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound)
        }
    }

    @Nested
    inner class Create {
        @Test
        fun `should create offer`() {
            val offerId = offerId()
            coEvery {
                createOfferUseCase.execute(any(), any(), 5.usd())
            } returns offer(offerId = offerId)

            val mvcResult = mockMvc.post("/offers") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(
                    createOfferRequest(
                        unitPrice = 5.toBigDecimal(),
                        currency = Currency.USD
                    )
                )
            }.andReturn()

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id").value(offerId.value.toString()))
                .andExpect(header().string("Location", "/offers/${offerId.value}"))

            coVerify {
                createOfferUseCase.execute(any(), any(), 5.usd())
            }
        }

        @ParameterizedTest
        @ValueSource(ints = [-5, 0])
        fun `should fail when unit price is not positive`(unitPrice: Int) {
            mockMvc.post("/offers") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(createOfferRequest(
                    unitPrice = unitPrice.toBigDecimal(),
                ))
            }.andExpect {
                status { isBadRequest() }
            }
        }
    }

    @Nested
    inner class ChangePrice {
        @Test
        fun `should change price`() {
            val offerId = offerId()
            coEvery { changePriceUseCase.execute(offerId, 1.usd()) } just Runs

            val mvcResult = mockMvc.patch("/offers/${offerId.value}") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(
                    ChangePriceRequest(
                        newPrice = 1.toBigDecimal(),
                        newCurrency = Currency.USD
                    )
                )
            }.andReturn()

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNoContent)

            coVerify {
                changePriceUseCase.execute(offerId, 1.usd())
            }
        }

        @ParameterizedTest
        @ValueSource(ints = [-1, 0])
        fun `should fail when unit price is not positive`(unitPrice: Int) {
            mockMvc.patch("/offers/${offerId().value}") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(
                    ChangePriceRequest(
                        newPrice = unitPrice.toBigDecimal(),
                        newCurrency = Currency.USD
                    )
                )
            }.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `should return 404 when offer does not exist`() {
            val offerId = offerId()

            coEvery {
                changePriceUseCase.execute(offerId, 1.usd())
            } throws OfferException.NotFound(offerId)

            val mvcResult = mockMvc.patch("/offers/${offerId.value}") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(
                    ChangePriceRequest(
                        newPrice = 1.toBigDecimal(),
                        newCurrency = Currency.USD
                    )
                )
            }.andReturn()

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound)
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `should delete offer`() {
            val offerId = offerId()
            coEvery { deleteOfferUseCase.execute(any()) } just Runs

            val mvcResult = mockMvc.delete("/offers/${offerId.value}").andReturn()
            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNoContent)

            coVerify(exactly = 1) {
                deleteOfferUseCase.execute(offerId)
            }
        }

        @Test
        fun `should return 404 when offer does not exist`() {
            val offerId = offerId()
            coEvery { deleteOfferUseCase.execute(offerId) } throws OfferException.NotFound(offerId)

            val mvcResult = mockMvc.delete("/offers/${offerId.value}").andReturn()
            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound)
        }
    }

    @Nested
    inner class List {
        @Test
        fun `should return first page`() = runTest {
            val request = PageRequest(
                pageSize = 20,
                cursor = null,
            )

            val page = Page(
                items = listOf(offer()),
                cursor = cursor("next-page"),
            )

            coEvery {
                listOffersUseCase.execute(request)
            } returns page

            val mvcResult = mockMvc.get("/offers").andReturn()

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.offers").isArray)
                .andExpect(jsonPath("$.offers.length()").value(1))
                .andExpect(jsonPath("$.nextPageToken").value("next-page"))
        }

        @Test
        fun `should pass page request`() = runTest {
            val request = PageRequest(
                pageSize = 5,
                cursor = cursor("abc"),
            )

            coEvery {
                listOffersUseCase.execute(request)
            } returns Page(
                items = emptyList(),
                cursor = null,
            )

            val mvcResult = mockMvc.get("/offers") {
                param("page_size", "5")
                param("page_token", "abc")
            }.andReturn()

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk)

            coVerify(exactly = 1) {
                listOffersUseCase.execute(request)
            }
        }

        @ParameterizedTest
        @ValueSource(ints = [-1, 0])
        fun `should fail when page size is not positive`(pageSize: Int) = runTest {
            val mvcResult = mockMvc.get("/offers") {
                param("page_size", pageSize.toString())
            }.andReturn()

            mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest)
        }
    }
}
