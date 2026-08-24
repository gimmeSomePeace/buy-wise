package me.gimmesomepeace.buywise.web.user

import me.gimmesomepeace.buywise.domain.user.UserException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(
    assignableTypes = [UserController::class],
)
class UserExceptionHandler {
    @ExceptionHandler(
        UserException.LoginBusy::class,
    )
    fun handleLoginBusy(e: UserException.LoginBusy) =
        ProblemDetail
            .forStatus(
                HttpStatus.CONFLICT,
            ).apply {
                title = "User already exists"
                detail =
                    "A user with login ${e.login.value} is already registered"
            }
}
