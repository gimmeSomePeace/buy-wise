package me.gimmesomepeace.buywise.web.store

import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.store.create.CreateStoreUseCase
import me.gimmesomepeace.buywise.application.store.delete.DeleteStoreUseCase
import me.gimmesomepeace.buywise.application.store.get.GetStoreUseCase
import me.gimmesomepeace.buywise.application.store.list.ListStoresUseCase
import me.gimmesomepeace.buywise.application.store.rename.RenameStoreUseCase
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.web.store.create.CreateStoreRequest
import me.gimmesomepeace.buywise.web.store.list.ListStoresResponse
import me.gimmesomepeace.buywise.web.store.rename.RenameStoreRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@Validated
@RequestMapping("/stores")
internal open class StoreController(
    private val getStoreUseCase: GetStoreUseCase,
    private val renameStoreUseCase: RenameStoreUseCase,
    private val createStoreUseCase: CreateStoreUseCase,
    private val deleteStoreUseCase: DeleteStoreUseCase,
    private val listStoresUseCase: ListStoresUseCase,
) {
    @GetMapping("/{id}")
    open suspend fun get(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable id: StoreId,
    ): ResponseEntity<StoreDetailsResponse> {
        val store = getStoreUseCase.execute(UserId(userId), id)
        return ResponseEntity.ok(store.toDetailsResponse())
    }

    @GetMapping
    open suspend fun list(
        @AuthenticationPrincipal userId: UUID,
        @RequestParam(
            value = "page_size",
            defaultValue = "20",
        )
        @Positive pageSize: Int,
        @RequestParam(value = "page_token", required = false) pageToken:
            String?,
    ): ResponseEntity<ListStoresResponse> {
        val cursor = pageToken?.let { Cursor(it) }
        val request = PageRequest(pageSize, cursor)
        val result = listStoresUseCase.execute(
            UserId(userId),
            request
        ).toListStoresResponse()
        return ResponseEntity.ok(result)
    }

    @PostMapping
    open suspend fun create(
        @AuthenticationPrincipal userId: UUID,
        @Valid @RequestBody request: CreateStoreRequest,
    ): ResponseEntity<StoreDetailsResponse> {
        val store =
            createStoreUseCase.execute(
                ownerId = UserId(userId),
                name = request.name,
            )
        return ResponseEntity
            .created(URI("/stores/${store.id}"))
            .body(store.toDetailsResponse())
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    open suspend fun rename(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable id: StoreId,
        @RequestBody request: RenameStoreRequest,
    ) {
        renameStoreUseCase.execute(
            userId = UserId(userId),
            storeId = id,
            newName = request.name,
        )
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    open suspend fun delete(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable id: StoreId,
    ) {
        deleteStoreUseCase.execute(
            userId = UserId(userId),
            storeId = id,
        )
    }
}
