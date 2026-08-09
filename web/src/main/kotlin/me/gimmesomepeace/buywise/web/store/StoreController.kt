package me.gimmesomepeace.buywise.web.store

import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.store.StoreQuery
import me.gimmesomepeace.buywise.application.store.create.CreateStoreUseCase
import me.gimmesomepeace.buywise.application.store.delete.DeleteStoreUseCase
import me.gimmesomepeace.buywise.application.store.list.ListStoresUseCase
import me.gimmesomepeace.buywise.application.store.rename.RenameStoreUseCase
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.web.store.create.CreateStoreRequest
import me.gimmesomepeace.buywise.web.store.list.ListStoresResponse
import me.gimmesomepeace.buywise.web.store.rename.RenameStoreRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@Validated
@RequestMapping("/stores")
internal open class StoreController(
    private val storeQuery: StoreQuery,
    private val renameStoreUseCase: RenameStoreUseCase,
    private val createStoreUseCase: CreateStoreUseCase,
    private val deleteStoreUseCase: DeleteStoreUseCase,
    private val listStoresUseCase: ListStoresUseCase,
) {
    @GetMapping("/{id}")
    open suspend fun get(
        @PathVariable id: StoreId,
    ): ResponseEntity<StoreDetailsResponse> {
        val store =
            storeQuery.find(id)
                ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(store.toDetailsResponse())
    }

    @GetMapping
    open suspend fun list(
        @RequestParam(value = "page_size", defaultValue = "20") @Positive pageSize: Int,
        @RequestParam(value = "page_token", required = false) pageToken: String?,
    ): ResponseEntity<ListStoresResponse> {
        val cursor = pageToken?.let { Cursor(it) }
        val request = PageRequest(pageSize, cursor)
        val result = listStoresUseCase.execute(request).toListStoresResponse()
        return ResponseEntity.ok(result)
    }

    @PostMapping
    open suspend fun create(
        @Valid @RequestBody request: CreateStoreRequest,
    ): ResponseEntity<StoreDetailsResponse> {
        val store =
            createStoreUseCase.execute(
                name = request.name,
            )
        return ResponseEntity
            .created(URI("/stores/${store.id}"))
            .body(store.toDetailsResponse())
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    open suspend fun rename(
        @PathVariable id: StoreId,
        @RequestBody request: RenameStoreRequest,
    ) {
        renameStoreUseCase.execute(
            storeId = id,
            newName = request.name,
        )
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    open suspend fun delete(
        @PathVariable id: StoreId,
    ) {
        deleteStoreUseCase.execute(
            storeId = id,
        )
    }
}
