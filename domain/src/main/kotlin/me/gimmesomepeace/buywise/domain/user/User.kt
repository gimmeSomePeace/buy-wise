package me.gimmesomepeace.buywise.domain.user

import me.gimmesomepeace.buywise.domain.shared.password.PasswordHash

class User(
    val id: UserId,
    val passwordHash: PasswordHash,
    val login: Login
)
