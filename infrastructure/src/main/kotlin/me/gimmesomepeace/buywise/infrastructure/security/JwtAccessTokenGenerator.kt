package me.gimmesomepeace.buywise.infrastructure.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import me.gimmesomepeace.buywise.application.auth.AccessToken
import me.gimmesomepeace.buywise.application.auth.AccessTokenGenerator
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.UserRole
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

class JwtAccessTokenGenerator(
    secretKey: String,
    private val expirationMs: Long = 86_400_000, // 1 день
) : AccessTokenGenerator {
    private val key: SecretKey = Keys.hmacShaKeyFor(secretKey.toByteArray())

    override fun generate(
        userId: UserId,
        role: UserRole,
    ): AccessToken {
        val now = Date()
        val expiry = Date(now.time + expirationMs)

        val token =
            Jwts
                .builder()
                .subject(userId.value.toString())
                .claim("role", role.name)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact()
        return AccessToken(token)
    }

    fun parse(token: AccessToken): ParsedToken {
        val claims =
            Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token.value)
                .payload
        val userId = UserId(UUID.fromString(claims.subject))
        val role = UserRole.valueOf(claims["role"] as String)
        return ParsedToken(userId, role)
    }

    data class ParsedToken(
        val userId: UserId,
        val role: UserRole,
    )
}
