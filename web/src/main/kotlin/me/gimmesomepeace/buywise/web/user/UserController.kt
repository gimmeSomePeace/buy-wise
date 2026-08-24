package me.gimmesomepeace.buywise.web.user

import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.user.list.ListUsersUseCase
import me.gimmesomepeace.buywise.application.user.reg.RegisterUserUseCase
import me.gimmesomepeace.buywise.domain.user.Login
import me.gimmesomepeace.buywise.web.user.list.ListUsersResponse
import me.gimmesomepeace.buywise.web.user.reg.RegisterUserRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/users")
@RestController
class UserController(
    private val registerUserUseCase: RegisterUserUseCase,
    private val listUsersUseCase: ListUsersUseCase,
) {
    @PostMapping
    suspend fun register(
        @Valid @RequestBody request: RegisterUserRequest,
    ): ResponseEntity<UserDetailsResponse> {
        val result =
            registerUserUseCase
                .execute(
                    login = Login(request.login),
                    password = request.password,
                ).toDetailsResponse()
        return ResponseEntity
            .status(
                201,
            ).body(result)
    }

    @GetMapping
    suspend fun list(
        @RequestParam(
            value = "page_size",
            defaultValue = "20",
        ) @Positive pageSize: Int,
        @RequestParam(
            value = "page_token",
            required = false,
        ) pageToken: String?,
    ): ResponseEntity<ListUsersResponse> {
        val pageRequest =
            pageToken
                ?.let {
                    PageRequest(
                        pageSize,
                        Cursor(it),
                    )
                }
                ?: PageRequest(pageSize)
        val users =
            listUsersUseCase.execute(
                pageRequest,
            )
        val result =
            ListUsersResponse(
                users =
                    users.items.map {
                        it
                            .toResponse()
                    },
                nextPageToken =
                    users.cursor
                        ?.value,
            )
        return ResponseEntity.ok(result)
    }
}
