package me.gimmesomepeace.buywise.app.offer

import me.gimmesomepeace.buywise.app.BeanNames
import me.gimmesomepeace.buywise.application.offer.OfferQuery
import me.gimmesomepeace.buywise.application.offer.create.CreateOfferUseCase
import me.gimmesomepeace.buywise.application.offer.delete.DeleteOfferUseCase
import me.gimmesomepeace.buywise.application.offer.list.ListOffersUseCase
import me.gimmesomepeace.buywise.application.offer.price.change.ChangeOfferPriceUseCase
import me.gimmesomepeace.buywise.application.shared.IdGenerator
import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.offer.OfferRepository
import me.gimmesomepeace.buywise.infrastructure.persistence.offer.OfferJpaRepository
import me.gimmesomepeace.buywise.infrastructure.persistence.offer.OfferQueryImpl
import me.gimmesomepeace.buywise.infrastructure.persistence.offer.OfferRepositoryImpl
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OfferConfiguration {
    @Bean
    fun offerRepository(offerJpaRepository: OfferJpaRepository): OfferRepository =
        OfferRepositoryImpl(offerJpaRepository)

    @Bean
    fun offerQuery(offerJpaRepository: OfferJpaRepository): OfferQuery = OfferQueryImpl(offerJpaRepository)

    @Bean
    fun createOfferUseCase(
        @Qualifier(BeanNames.OFFER_ID_GENERATOR)
        idGenerator: IdGenerator<OfferId>,
        repository: OfferRepository,
        query: OfferQuery,
    ) = CreateOfferUseCase(
        idGenerator,
        repository,
        query,
    )

    @Bean
    fun deleteOfferUseCase(
        repository: OfferRepository,
        query: OfferQuery,
    ) = DeleteOfferUseCase(repository, query)

    @Bean
    fun listOffersUseCase(query: OfferQuery) = ListOffersUseCase(query)

    @Bean
    fun changePriceUseCase(
        repository: OfferRepository,
        query: OfferQuery,
    ) = ChangeOfferPriceUseCase(repository, query)
}
