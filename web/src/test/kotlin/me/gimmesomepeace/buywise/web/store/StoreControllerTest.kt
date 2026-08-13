package me.gimmesomepeace.buywise.web.store

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.shared.cursor
import me.gimmesomepeace.buywise.application.store.StoreQuery
import me.gimmesomepeace.buywise.application.store.create.CreateStoreUseCase
import me.gimmesomepeace.buywise.application.store.delete.DeleteStoreUseCase
import me.gimmesomepeace.buywise.application.store.list.ListStoresUseCase
import me.gimmesomepeace.buywise.application.store.rename.RenameStoreUseCase
import me.gimmesomepeace.buywise.domain.store.StoreException
import me.gimmesomepeace.buywise.domain.store.store
import me.gimmesomepeace.buywise.domain.store.storeId
import me.gimmesomepeace.buywise.domain.user.userId
import me.gimmesomepeace.buywise.web.TestSecurityConfig
import me.gimmesomepeace.buywise.web.authenticatedAs
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import tools.jackson.databind.ObjectMapper

@WebMvcTest(StoreController::class)
@Import(TestSecurityConfig::class)
class StoreControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @MockkBean
    lateinit var storeQuery: StoreQuery

    @MockkBean
    lateinit var createStoreUseCase: CreateStoreUseCase

    @MockkBean
    lateinit var deleteStoreUseCase: DeleteStoreUseCase

    @MockkBean
    lateinit var renameStoreUseCase: RenameStoreUseCase

    @MockkBean
    lateinit var listStoresUseCase: ListStoresUseCase

    @Nested
    inner class Get {
        @Test
        fun `should return store`() =
            runTest {
                val store = store()

                coEvery {
                    storeQuery.find(store.id)
                } returns store

                val mvcResult =
                    mockMvc
                        .get("/stores/${store.id.value}")
                        .andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk)
                    .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON),
                    ).andExpect(
                        jsonPath("$.id").value(store.id.value.toString()),
                    )
            }

        @Test
        fun `should return 404 when store not found`() =
            runTest {
                val storeId = storeId()

                coEvery {
                    storeQuery.find(storeId)
                } returns null

                val mvcResult =
                    mockMvc
                        .get("/stores/${storeId.value}")
                        .andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isNotFound)
            }
    }

    @Nested
    inner class Create {
        @Test
        fun `should create store`() =
            runTest {
                val ownerId = userId()
                val store = store()

                coEvery {
                    createStoreUseCase.execute(ownerId, store.name)
                } returns store

                val mvcResult =
                    mockMvc
                        .post("/stores") {
                            with(authenticatedAs(ownerId))
                            contentType = MediaType.APPLICATION_JSON
                            content =
                                mapper.writeValueAsString(
                                    createStoreRequest(store.name),
                                )
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isCreated)
                    .andExpect(
                        header().string("Location", "/stores/${store.id}"),
                    ).andExpect(
                        content().contentType(MediaType.APPLICATION_JSON),
                    ).andExpect(
                        jsonPath("$.id").value(store.id.value.toString()),
                    ).andExpect(jsonPath("$.name").value(store.name))

                coVerify(exactly = 1) {
                    createStoreUseCase.execute(ownerId, store.name)
                }
            }

        @ParameterizedTest
        @ValueSource(strings = ["", "   "])
        fun `should fail when name is invalid`(
            name: String,
        ) = runTest {
            mockMvc
                .post("/stores") {
                    contentType = MediaType.APPLICATION_JSON
                    content =
                        mapper.writeValueAsString(
                            createStoreRequest(name),
                        )
                }.andExpect {
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    inner class Rename {
        @Test
        fun `should rename store`() =
            runTest {
                val storeId = storeId()

                coJustRun {
                    renameStoreUseCase.execute(storeId, "NEW NAME")
                }

                val mvcResult =
                    mockMvc
                        .patch("/stores/${storeId.value}") {
                            contentType = MediaType.APPLICATION_JSON
                            content =
                                mapper.writeValueAsString(
                                    renameStoreRequest("NEW NAME"),
                                )
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isNoContent)

                coVerify(exactly = 1) {
                    renameStoreUseCase.execute(storeId, "NEW NAME")
                }
            }

        @Test
        fun `should return 404 when store is not found`() =
            runTest {
                val storeId = storeId()
                coEvery {
                    renameStoreUseCase.execute(storeId, any())
                } throws StoreException.NotFound(storeId)

                val mvcResult =
                    mockMvc
                        .patch("/stores/${storeId.value}") {
                            contentType = MediaType.APPLICATION_JSON
                            content =
                                mapper.writeValueAsString(
                                    renameStoreRequest(),
                                )
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isNotFound)
            }
    }

    @Nested
    inner class Delete {
        @Test
        fun `should delete store`() =
            runTest {
                val storeId = storeId()

                coJustRun {
                    deleteStoreUseCase.execute(storeId)
                }

                val mvcResult =
                    mockMvc
                        .delete("/stores/${storeId.value}")
                        .andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isNoContent)

                coVerify(exactly = 1) {
                    deleteStoreUseCase.execute(storeId)
                }
            }

        @Test
        fun `should return 404 when store is not found`() =
            runTest {
                val storeId = storeId()
                coEvery {
                    deleteStoreUseCase.execute(storeId)
                } throws StoreException.NotFound(storeId)

                val mvcResult =
                    mockMvc
                        .delete("/stores/${storeId.value}")
                        .andReturn()

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
                val ownerId = userId()
                val request =
                    PageRequest(
                        pageSize = 20,
                        cursor = null,
                    )

                val page =
                    Page(
                        items = listOf(store()),
                        cursor = cursor("next-page"),
                    )

                coEvery {
                    listStoresUseCase.execute(ownerId, request)
                } returns page

                val mvcResult = mockMvc.get("/stores") {
                    with(authenticatedAs(ownerId))
                }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk)
                    .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON),
                    ).andExpect(jsonPath("$.stores").isArray)
                    .andExpect(jsonPath("$.stores.length()").value(1))
                    .andExpect(jsonPath("$.nextPageToken").value("next-page"))
            }

        @Test
        fun `should pass page request`() =
            runTest {
                val ownerId = userId()
                val request =
                    PageRequest(
                        pageSize = 5,
                        cursor = cursor("abc"),
                    )

                coEvery {
                    listStoresUseCase.execute(ownerId = ownerId, request)
                } returns
                    Page(
                        items = emptyList(),
                        cursor = null,
                    )

                val mvcResult =
                    mockMvc
                        .get("/stores") {
                            param("page_size", "5")
                            param("page_token", "abc")
                            with(authenticatedAs(ownerId))
                        }.andReturn()

                mockMvc
                    .perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk)

                coVerify(exactly = 1) {
                    listStoresUseCase.execute(ownerId, request)
                }
            }

        @ParameterizedTest
        @ValueSource(ints = [-1, 0])
        fun `should fail when page size is not positive`(
            pageSize: Int,
        ) = runTest {
            val mvcResult =
                mockMvc
                    .get("/stores") {
                        param("page_size", pageSize.toString())
                        with(authenticatedAs(userId()))
                    }.andReturn()
            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest)
        }
    }
}
