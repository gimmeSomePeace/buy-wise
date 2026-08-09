package me.gimmesomepeace.buywise.web.store

import me.gimmesomepeace.buywise.domain.store.StoreException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [StoreController::class])
internal class StoreExceptionHandler {
    @ExceptionHandler(StoreException.NotFound::class)
    fun handleNotFound(exc: StoreException.NotFound) =
        ProblemDetail.forStatus(HttpStatus.NOT_FOUND).apply {
            title = "Offer Not Found"
            detail = "Offer with id ${exc.storeId} not found"
        }

    @ExceptionHandler(StoreException.AlreadyExists::class)
    fun handleAlreadyExists(exc: StoreException.AlreadyExists) =
        ProblemDetail.forStatus(HttpStatus.CONFLICT).apply {
            title = "Offer already exists"
            detail = "Offer with id ${exc.storeId} already exists"
        }
}
