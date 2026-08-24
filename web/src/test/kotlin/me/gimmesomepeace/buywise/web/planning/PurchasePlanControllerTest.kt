package me.gimmesomepeace.buywise.web.planning

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.planning.plan.CreatePurchasePlanUseCase
import me.gimmesomepeace.buywise.domain.planning.PurchasePlanningResult
import me.gimmesomepeace.buywise.web.planning.purchase.CreatePurchasePlanRequest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.jackson.databind.ObjectMapper

@WebMvcTest(PurchasePlanController::class)
class PurchasePlanControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @MockkBean
    lateinit var createPurchasePlanUseCase: CreatePurchasePlanUseCase

    @Nested
    inner class PurchasePlan {
        @Test
        fun `should create purchase plan`() =
            runTest {
                val result =
                    PurchasePlanningResult.Success(
                        plans = emptyList(),
                    )

                coEvery {
                    createPurchasePlanUseCase.execute(2)
                } returns result

                val mvcResult =
                    mockMvc
                        .post("/purchase-plan") {
                            contentType = MediaType.APPLICATION_JSON
                            content =
                                mapper.writeValueAsString(
                                    CreatePurchasePlanRequest(
                                        storeCountLimit = 2,
                                    ),
                                )
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk)
                    .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON),
                    )

                coVerify(exactly = 1) {
                    createPurchasePlanUseCase.execute(2)
                }
            }

        @ParameterizedTest
        @ValueSource(ints = [0, -1])
        fun `should fail when store count limit is invalid`(limit: Int) =
            runTest {
                mockMvc
                    .post("/purchase-plan") {
                        contentType = MediaType.APPLICATION_JSON
                        content =
                            mapper.writeValueAsString(
                                CreatePurchasePlanRequest(
                                    storeCountLimit = limit,
                                ),
                            )
                    }.andExpect {
                        status { isBadRequest() }
                    }

                coVerify(exactly = 0) {
                    createPurchasePlanUseCase.execute(any())
                }
            }
    }
}
