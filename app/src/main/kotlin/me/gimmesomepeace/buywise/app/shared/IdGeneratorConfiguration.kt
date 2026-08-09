package me.gimmesomepeace.buywise.app.shared

import me.gimmesomepeace.buywise.app.BeanNames
import me.gimmesomepeace.buywise.application.shared.IdGenerator
import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.infrastructure.persistence.shared.UuidV7Generator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.UUID

@Configuration
class IdGeneratorConfiguration {
    @Bean
    fun uuidV7Generator(): IdGenerator<UUID> = UuidV7Generator()

    @Bean(BeanNames.PRODUCT_ID_GENERATOR)
    fun productIdGenerator(idGenerator: IdGenerator<UUID>) = IdGenerator { ProductId(idGenerator.generate()) }

    @Bean(BeanNames.STORE_ID_GENERATOR)
    fun storeIdGenerator(idGenerator: IdGenerator<UUID>) = IdGenerator { StoreId(idGenerator.generate()) }

    @Bean(BeanNames.OFFER_ID_GENERATOR)
    fun offerIdGenerator(idGenerator: IdGenerator<UUID>) = IdGenerator { OfferId(idGenerator.generate()) }
}
