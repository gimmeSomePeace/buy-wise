package me.gimmesomepeace.buywise.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(
    scanBasePackages = [
        "me.gimmesomepeace.buywise.app",
        "me.gimmesomepeace.buywise.web",
    ]
)
@EntityScan("me.gimmesomepeace.buywise.infrastructure.persistence")
@EnableJpaRepositories("me.gimmesomepeace.buywise.infrastructure.persistence")
class BuyWiseApplication

fun main(args: Array<String>) {
    runApplication<BuyWiseApplication>(*args)
}
