package me.gimmesomepeace.buywise.app.basket

import me.gimmesomepeace.buywise.application.basket.BasketQuery
import me.gimmesomepeace.buywise.application.basket.add.AddProductToBasketUseCase
import me.gimmesomepeace.buywise.application.basket.change.ChangeBasketItemQuantityUseCase
import me.gimmesomepeace.buywise.application.basket.clear.ClearBasketUseCase
import me.gimmesomepeace.buywise.application.basket.remove.RemoveFromBasketUseCase
import me.gimmesomepeace.buywise.domain.basket.BasketRepository
import me.gimmesomepeace.buywise.infrastructure.persistence.basket.BasketJpaRepository
import me.gimmesomepeace.buywise.infrastructure.persistence.basket.BasketQueryImpl
import me.gimmesomepeace.buywise.infrastructure.persistence.basket.BasketRepositoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BasketConfiguration {
    @Bean
    fun basketRepository(
        basketJpaRepository: BasketJpaRepository
    ) : BasketRepository = BasketRepositoryImpl(basketJpaRepository)

    @Bean
    fun basketQuery(
        basketJpaRepository: BasketJpaRepository
    ) : BasketQuery = BasketQueryImpl(basketJpaRepository)

    @Bean
    fun addProductToBasketUseCase(
        repository: BasketRepository
    ) = AddProductToBasketUseCase(repository)

    @Bean
    fun changeBasketItemQuantityUseCase(
        repository: BasketRepository
    ) = ChangeBasketItemQuantityUseCase(repository)

    @Bean
    fun clearBasketUseCase(repository: BasketRepository) =
        ClearBasketUseCase(repository)

    @Bean
    fun removeFromBasketUseCase(
        repository: BasketRepository
    ) = RemoveFromBasketUseCase(repository)
}
