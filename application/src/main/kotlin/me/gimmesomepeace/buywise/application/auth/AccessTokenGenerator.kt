package me.gimmesomepeace.buywise.application.auth

import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.UserRole

fun interface AccessTokenGenerator {
    fun generate(
        userId: UserId,
        role: UserRole,
    ): AccessToken
}
