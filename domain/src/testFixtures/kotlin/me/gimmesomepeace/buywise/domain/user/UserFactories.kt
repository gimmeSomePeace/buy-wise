package me.gimmesomepeace.buywise.domain.user

import com.github.f4b6a3.uuid.UuidCreator
import me.gimmesomepeace.buywise.domain.shared.password.PasswordHash

fun userId() = UserId(UuidCreator.getTimeOrderedEpoch())

fun login(
    value: String = "LOGIN"
) = Login(value)

fun user(
    id: UserId = userId(),
    login: Login = login(),
    passwordHash: PasswordHash = PasswordHash("password-hash"),
    role: UserRole = UserRole.USER
) = User(id, login, passwordHash, role)

