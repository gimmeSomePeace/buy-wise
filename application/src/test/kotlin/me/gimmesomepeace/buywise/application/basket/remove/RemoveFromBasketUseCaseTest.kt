package me.gimmesomepeace.buywise.application.basket.remove

import io.mockk.*
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.basket.BasketException
import me.gimmesomepeace.buywise.domain.basket.BasketRepository
import me.gimmesomepeace.buywise.domain.basket.basket
import me.gimmesomepeace.buywise.domain.basket.getOrEmpty
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Quantity
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class RemoveFromBasketUseCaseTest {
    private val basketRepository = mockk<BasketRepository>()
    private val useCase = RemoveFromBasketUseCase(basketRepository)

    @Test
    fun `should remove existing product from basket`() = runTest {
        val productId = productId()
        val basket = basket {
            add(productId, Quantity.ONE)
        }

        coEvery { basketRepository.getOrEmpty() } returns basket
        coEvery { basketRepository.save(any()) } just runs

        useCase.execute(productId)

        coVerify(exactly = 1) {
            basketRepository.save(
                match { it.quantityOf(productId) == Quantity.ZERO }
            )
        }
    }

    @Test
    fun `should throw when product not in basket`() = runTest {
        val productId = productId()
        val emptyBasket = basket()

        coEvery { basketRepository.getOrEmpty() } returns emptyBasket

        assertFailsWith<BasketException.ProductNotInBasket> {
            useCase.execute(productId)
        }

        coVerify(exactly = 0) { basketRepository.save(any()) }
    }
}
