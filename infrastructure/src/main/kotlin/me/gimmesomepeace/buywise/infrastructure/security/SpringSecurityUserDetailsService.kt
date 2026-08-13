package me.gimmesomepeace.buywise.infrastructure.security

import kotlinx.coroutines.runBlocking
import me.gimmesomepeace.buywise.application.user.UserQuery
import me.gimmesomepeace.buywise.domain.user.Login
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class SpringSecurityUserDetailsService(
    private val query: UserQuery
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val login = Login(username)
        val user = runBlocking {
            query.findByLogin(login)
        } ?: throw UsernameNotFoundException(username)

        return SpringUserDetails(user)
    }
}
