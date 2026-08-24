package me.gimmesomepeace.buywise.app.security

import me.gimmesomepeace.buywise.infrastructure.security.JwtAccessTokenGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TokenConfig {
    @Bean
    fun tokenGenerator(
        @Value($$"${jwt.secret}") secretKey: String,
    ): JwtAccessTokenGenerator = JwtAccessTokenGenerator(secretKey)
}
