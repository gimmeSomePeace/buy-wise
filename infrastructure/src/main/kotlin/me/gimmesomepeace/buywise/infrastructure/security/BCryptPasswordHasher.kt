package me.gimmesomepeace.buywise.infrastructure.security

import me.gimmesomepeace.buywise.domain.shared.password.PasswordHash
import me.gimmesomepeace.buywise.domain.shared.password.PasswordHasher
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class BCryptPasswordHasher(
    strength: Int = 12,
) : PasswordHasher {
    private val encoder =
        BCryptPasswordEncoder(strength)

    override fun hash(password: String): PasswordHash {
        val hashed =
            encoder.encode(password)
                ?: throw IllegalArgumentException(
                    "password must not be null",
                )
        return PasswordHash(hashed)
    }

    override fun matches(
        password: String,
        hash: PasswordHash,
    ): Boolean = encoder.matches(password, hash.value)
}
