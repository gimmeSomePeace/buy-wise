package me.gimmesomepeace.buywise.application.basket.add

import io.mockk.*
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.basket.BasketRepository
import me.gimmesomepeace.buywise.domain.basket.basket
import me.gimmesomepeace.buywise.domain.basket.getOrEmpty
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.qty
import org.junit.jupiter.api.Test

class AddProductToBasketUseCaseTest {
    private val basketRepository = mockk<BasketRepository>()
    private val useCase = AddProductToBasketUseCase(basketRepository)

    @Test
    fun `should add product to empty basket`() = runTest {
        val productId = productId()
        val quantity = 3.qty()
        val emptyBasket = basket()

        coEvery { basketRepository.getOrEmpty() } returns emptyBasket
        coEvery { basketRepository.save(any()) } just runs

        useCase.execute(productId, quantity)

        coVerify(exactly = 1) {
            basketRepository.save(
                match { it.quantityOf(productId) == quantity }
            )
        }
    }

    @Test
    fun `should increase quantity when product already in basket`() = runTest {
        val productId = productId()
        val existingBasket = basket {
            add(productId, 2.qty())
        }

        coEvery { basketRepository.getOrEmpty() } returns existingBasket
        coEvery { basketRepository.save(any()) } just runs

        useCase.execute(productId, 3.qty())

        coVerify(exactly = 1) {
            basketRepository.save(
                match { it.quantityOf(productId) == 5.qty() }
            )
        }
    }

    @Test
    fun `should save basket exactly once`() = runTest {
        val productId = productId()
        val emptyBasket = basket()

        coEvery { basketRepository.getOrEmpty() } returns emptyBasket
        coEvery { basketRepository.save(any()) } just runs

        useCase.execute(productId, 1.qty())

        coVerify(exactly = 1) { basketRepository.save(any()) }
    }
}
