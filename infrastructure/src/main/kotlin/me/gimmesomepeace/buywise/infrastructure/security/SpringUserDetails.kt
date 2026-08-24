package me.gimmesomepeace.buywise.infrastructure.security

import me.gimmesomepeace.buywise.application.user.UserView
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class SpringUserDetails(
    private val user: UserView,
) : UserDetails {
    override fun getUsername(): String = user.login.value

    override fun getPassword(): String = user.passwordHash.value

    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
}
