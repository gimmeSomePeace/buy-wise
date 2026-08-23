package me.gimmesomepeace.buywise.domain.user

import com.github.f4b6a3.uuid.UuidCreator
import me.gimmesomepeace.buywise.domain.shared.password.PasswordHash
import java.util.concurrent.atomic.AtomicLong

private val loginCounter = AtomicLong(0)

fun userId() = UserId(UuidCreator.getTimeOrderedEpoch())

fun login(
    value: String = "LOGIN-${loginCounter.incrementAndGet()}",
) = Login(value)

fun user(
    id: UserId = userId(),
    login: Login = login(),
    passwordHash: PasswordHash = PasswordHash("password-hash"),
    role: UserRole = UserRole.USER
) = User(id, login, passwordHash, role)
