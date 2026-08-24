package me.gimmesomepeace.buywise.application.user

import me.gimmesomepeace.buywise.domain.shared.password.PasswordHash
import me.gimmesomepeace.buywise.domain.user.Login
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.UserRole
import me.gimmesomepeace.buywise.domain.user.login
import me.gimmesomepeace.buywise.domain.user.userId

fun userDetails(
    id: UserId = userId(),
    login: Login = login(),
    passwordHash: PasswordHash =
        PasswordHash("password-hash"),
    role: UserRole = UserRole.USER,
) = UserView(
    id = id,
    login = login,
    passwordHash = passwordHash,
    role = role,
)
