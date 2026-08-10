package me.gimmesomepeace.buywise.app.planning

import me.gimmesomepeace.buywise.application.planning.plan.CreatePurchasePlanUseCase
import me.gimmesomepeace.buywise.domain.basket.BasketRepository
import me.gimmesomepeace.buywise.domain.offer.OfferRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PlanningConfiguration {
    @Bean
    fun planningUseCase(
        basketRepository: BasketRepository,
        offerRepository: OfferRepository,
    ) = CreatePurchasePlanUseCase(
        basketRepository,
        offerRepository,
    )
}
