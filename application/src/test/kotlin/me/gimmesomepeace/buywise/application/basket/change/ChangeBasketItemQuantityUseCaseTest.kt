package me.gimmesomepeace.buywise.application.basket.change

import io.mockk.*
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.basket.BasketRepository
import me.gimmesomepeace.buywise.domain.basket.basket
import me.gimmesomepeace.buywise.domain.basket.getOrEmpty
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.qty
import kotlin.test.Test

class ChangeBasketItemQuantityUseCaseTest {
    private val basketRepository = mockk<BasketRepository>()
    private val useCase = ChangeBasketItemQuantityUseCase(basketRepository)

    @Test
    fun `should change quantity of existing item`() = runTest {
        val productId = productId()
        val existingBasket = basket {
            add(productId, 3.qty())
        }

        coEvery { basketRepository.getOrEmpty() } returns existingBasket
        coEvery { basketRepository.save(any()) } just runs

        useCase.execute(productId, 5.qty())

        coVerify(exactly = 1) {
            basketRepository.save(
                match { it.quantityOf(productId) == 5.qty() }
            )
        }
    }

    @Test
    fun `should add item when not in basket`() = runTest {
        val productId = productId()
        val emptyBasket = basket()

        coEvery { basketRepository.getOrEmpty() } returns emptyBasket
        coEvery { basketRepository.save(any()) } just runs

        useCase.execute(productId, 5.qty())

        coVerify(exactly = 1) {
            basketRepository.save(
                match { it.quantityOf(productId) == 5.qty() }
            )
        }
    }
}
