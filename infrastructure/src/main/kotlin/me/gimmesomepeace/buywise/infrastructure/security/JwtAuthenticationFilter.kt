package me.gimmesomepeace.buywise.infrastructure.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.gimmesomepeace.buywise.application.auth.AccessToken
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtGenerator: JwtAccessTokenGenerator,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token = extractToken(request)

        if (token != null) {
            try {
                val parsed = jwtGenerator.parse(token)
                val authentication =
                    UsernamePasswordAuthenticationToken(
                        parsed.userId,
                        null,
                        listOf(
                            SimpleGrantedAuthority("ROLE_${parsed.role.name}"),
                        ),
                    )
                authentication.isAuthenticated = true

                SecurityContextHolder.getContext().authentication =
                    authentication
            } catch (_: Exception) {
                SecurityContextHolder.clearContext()
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): AccessToken? {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header == null || !header.startsWith("Bearer ")) return null

        val raw = header.removePrefix("Bearer ")
        return runCatching { AccessToken(raw) }.getOrNull()
    }
}
