package me.gimmesomepeace.buywise.infrastructure

import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class PostgresSqlContainer {
    companion object {
        @Container
        @JvmStatic
        @ServiceConnection
        val postgres =
            PostgreSQLContainer("postgres:17")
    }
}
