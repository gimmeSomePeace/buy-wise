package me.gimmesomepeace.buywise.web.offer

import me.gimmesomepeace.buywise.domain.offer.OfferException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [OfferController::class])
internal class OfferExceptionHandler {
    @ExceptionHandler(OfferException.NotFound::class)
    fun handleNotFound(
        ex: OfferException.NotFound,
    ) = ProblemDetail.forStatus(HttpStatus.NOT_FOUND).apply {
        title = "Offer not found"
        detail = "Offer with id ${ex.offerId} not found"
    }

    @ExceptionHandler(OfferException.AlreadyExists::class)
    fun handleAlreadyExists(
        ex: OfferException.AlreadyExists,
    ) = ProblemDetail.forStatus(HttpStatus.CONFLICT).apply {
        title = "Offer already exists"
        detail = "Offer with id ${ex.offerId} already exists"
    }
}
