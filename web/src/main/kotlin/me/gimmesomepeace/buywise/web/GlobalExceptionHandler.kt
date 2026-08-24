package me.gimmesomepeace.buywise.web

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(
        ConstraintViolationException::class,
    )
    fun handle(ex: ConstraintViolationException): ProblemDetail =
        ProblemDetail
            .forStatus(
                HttpStatus.BAD_REQUEST,
            ).apply {
                title = "Validation failed"
                detail = ex.message
            }
}
