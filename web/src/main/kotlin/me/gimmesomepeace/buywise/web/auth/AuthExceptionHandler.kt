package me.gimmesomepeace.buywise.web.auth

import me.gimmesomepeace.buywise.application.auth.AuthenticationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [AuthController::class])
internal class AuthExceptionHandler {
    @ExceptionHandler(AuthenticationException.InvalidCredentials::class)
    fun handleInvalidCredentials(
        e: AuthenticationException.InvalidCredentials
    ) : ResponseEntity<ProblemDetail> {
        val problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED).apply {
            title = "Invalid credentials"
            detail = "login or password is incorrect"
        }

        return ResponseEntity
            .status(problem.status)
            .header("WWW-Authenticate", "Bearer realm=\"buywise\"")
            .body(problem)
    }
}
