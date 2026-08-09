package me.gimmesomepeace.buywise.web.product

import me.gimmesomepeace.buywise.domain.product.ProductException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [ProductController::class])
internal class ProductExceptionHandler {
    @ExceptionHandler(ProductException.NotFound::class)
    fun handleNotFound(ex: ProductException.NotFound) =
        ProblemDetail.forStatus(HttpStatus.NOT_FOUND).apply {
            title = "Product not found"
            detail = "Product with id ${ex.productId} not found}"
        }

    @ExceptionHandler(ProductException.AlreadyExists::class)
    fun handleAlreadyExists(ex: ProductException.AlreadyExists) =
        ProblemDetail.forStatus(HttpStatus.CONFLICT).apply {
            title = "Product already exists"
            detail = "Product with id ${ex.productId} already exists"
        }
}