package me.gimmesomepeace.buywise.web

import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.UserRole
import me.gimmesomepeace.buywise.domain.user.userId
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication

fun authenticatedAs(userId: UserId = userId()) =
    authentication(
        UsernamePasswordAuthenticationToken(
            userId.value,
            null,
            listOf(
                SimpleGrantedAuthority(
                    "ROLE_${UserRole.USER.name}",
                ),
            ),
        ),
    )
