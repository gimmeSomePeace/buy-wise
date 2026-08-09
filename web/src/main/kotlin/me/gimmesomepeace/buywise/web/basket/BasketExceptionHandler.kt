package me.gimmesomepeace.buywise.web.basket

import me.gimmesomepeace.buywise.domain.basket.BasketException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [BasketController::class])
internal class BasketExceptionHandler {
    @ExceptionHandler(BasketException.ProductNotInBasket::class)
    fun handleProductNotInBasket(ex: BasketException.ProductNotInBasket) =
        ProblemDetail.forStatus(HttpStatus.NOT_FOUND).apply {
            title = "Product not in basket"
            detail = "Product with id ${ex.productId} not in basket"
        }
}
