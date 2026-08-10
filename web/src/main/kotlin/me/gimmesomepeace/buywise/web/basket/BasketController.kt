package me.gimmesomepeace.buywise.web.basket

import jakarta.validation.Valid
import me.gimmesomepeace.buywise.application.basket.BasketDetails
import me.gimmesomepeace.buywise.application.basket.BasketQuery
import me.gimmesomepeace.buywise.application.basket.add.AddProductToBasketUseCase
import me.gimmesomepeace.buywise.application.basket.change.ChangeBasketItemQuantityUseCase
import me.gimmesomepeace.buywise.application.basket.clear.ClearBasketUseCase
import me.gimmesomepeace.buywise.application.basket.remove.RemoveFromBasketUseCase
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Quantity
import me.gimmesomepeace.buywise.web.basket.add.AddProductToBasketRequest
import me.gimmesomepeace.buywise.web.basket.change.ChangeProductQuantityRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/basket")
internal class BasketController(
    private val basketQuery: BasketQuery,
    private val changeBasketItemQuantityUseCase:
        ChangeBasketItemQuantityUseCase,
    private val addProductToBasketUseCase: AddProductToBasketUseCase,
    private val clearBasketUseCase: ClearBasketUseCase,
    private val removeFromBasketUseCase: RemoveFromBasketUseCase,
) {
    @GetMapping
    suspend fun get(): ResponseEntity<BasketDetails> {
        val basket =
            basketQuery.find()
                ?: BasketDetails(emptyList())
        return ResponseEntity.ok(basket)
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun add(
        @Valid @RequestBody request: AddProductToBasketRequest,
    ) {
        addProductToBasketUseCase.execute(
            productId = ProductId(request.productId),
            quantity = Quantity(request.quantity),
        )
    }

    @PutMapping("/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun changeProductQuantity(
        @PathVariable id: ProductId,
        @Valid @RequestBody request: ChangeProductQuantityRequest,
    ) {
        changeBasketItemQuantityUseCase.execute(
            productId = id,
            quantity = Quantity(request.newQuantity),
        )
    }

    @DeleteMapping("/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun remove(
        @PathVariable id: ProductId,
    ) {
        removeFromBasketUseCase.execute(
            productId = id,
        )
    }

    @DeleteMapping("/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun clear() {
        clearBasketUseCase.execute()
    }
}
