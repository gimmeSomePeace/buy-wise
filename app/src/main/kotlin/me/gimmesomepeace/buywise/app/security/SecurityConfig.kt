package me.gimmesomepeace.buywise.app.security

import me.gimmesomepeace.buywise.application.user.UserQuery
import me.gimmesomepeace.buywise.domain.shared.password.PasswordHasher
import me.gimmesomepeace.buywise.infrastructure.security.BCryptPasswordHasher
import me.gimmesomepeace.buywise.infrastructure.security.JwtAccessTokenGenerator
import me.gimmesomepeace.buywise.infrastructure.security.JwtAuthenticationFilter
import me.gimmesomepeace.buywise.infrastructure.security.SpringSecurityUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtGenerator: JwtAccessTokenGenerator,
    private val userQuery: UserQuery,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val jwtFilter = JwtAuthenticationFilter(jwtGenerator)

        http
            .csrf { it.disable() }
            .cors { }
            .sessionManagement {
                it.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS,
                )
            }.authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/auth/login",
                        "/auth/register",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                    ).permitAll()
                    .requestMatchers("/admin/**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated()
            }.addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter::class.java,
            )
        return http.build()
    }

    @Bean
    fun passwordHasher(): PasswordHasher = BCryptPasswordHasher()

    @Bean
    fun userDetailsService(): UserDetailsService = SpringSecurityUserDetailsService(userQuery)
}
