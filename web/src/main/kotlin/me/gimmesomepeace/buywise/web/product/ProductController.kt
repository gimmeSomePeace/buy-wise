package me.gimmesomepeace.buywise.web.product

import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import me.gimmesomepeace.buywise.application.product.create.CreateProductUseCase
import me.gimmesomepeace.buywise.application.product.delete.DeleteProductUseCase
import me.gimmesomepeace.buywise.application.product.get.GetProductUseCase
import me.gimmesomepeace.buywise.application.product.list.ListProductsUseCase
import me.gimmesomepeace.buywise.application.product.rename.RenameProductUseCase
import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.web.product.create.CreateProductRequest
import me.gimmesomepeace.buywise.web.product.list.ListProductsResponse
import me.gimmesomepeace.buywise.web.product.rename.RenameProductRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
import java.util.UUID

@RestController
@Validated
@RequestMapping("/products")
internal open class ProductController(
    private val getProductUseCase: GetProductUseCase,
    private val listProductsUseCase: ListProductsUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val renameProductUseCase: RenameProductUseCase,
) {
    @GetMapping("/{id}")
    open suspend fun get(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable id: ProductId,
    ): ResponseEntity<ProductDetailsResponse> {
        val product =
            getProductUseCase.execute(
                UserId(userId),
                id,
            )
        return ResponseEntity.ok(
            product.toDetailsResponse(),
        )
    }

    @GetMapping
    open suspend fun list(
        @AuthenticationPrincipal userId: UUID,
        @RequestParam(
            value = "page_size",
            defaultValue = "20",
        ) @Positive pageSize: Int,
        @RequestParam(
            value = "page_token",
            required = false,
        ) pageToken: String?,
    ): ResponseEntity<ListProductsResponse> {
        val cursor = pageToken?.let { Cursor(it) }
        val request =
            PageRequest(pageSize, cursor)
        val result =
            listProductsUseCase
                .execute(
                    ownerId = UserId(userId),
                    request,
                ).toListProductsResponse()
        return ResponseEntity.ok(result)
    }

    @PostMapping
    open suspend fun create(
        @AuthenticationPrincipal userId: UUID,
        @Valid @RequestBody request: CreateProductRequest,
    ): ResponseEntity<ProductDetailsResponse> {
        val product =
            createProductUseCase
                .execute(
                    UserId(userId),
                    request.name,
                )
        return ResponseEntity
            .created(
                URI(
                    "/products/${product.id.value}",
                ),
            ).body(product.toDetailsResponse())
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    open suspend fun delete(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable id: ProductId,
    ) {
        deleteProductUseCase.execute(
            userId = UserId(userId),
            productId = id,
        )
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    open suspend fun rename(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable id: ProductId,
        @RequestBody request: RenameProductRequest,
    ) {
        renameProductUseCase.execute(
            userId = UserId(userId),
            productId = id,
            newName = request.name,
        )
    }
}
