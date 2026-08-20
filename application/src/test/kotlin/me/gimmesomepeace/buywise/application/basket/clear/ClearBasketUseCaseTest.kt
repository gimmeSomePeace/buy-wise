package me.gimmesomepeace.buywise.application.basket.clear

import io.mockk.*
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.basket.BasketRepository
import me.gimmesomepeace.buywise.domain.basket.basket
import me.gimmesomepeace.buywise.domain.basket.getOrEmpty
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Quantity
import me.gimmesomepeace.buywise.domain.shared.qty
import org.junit.jupiter.api.Test

class ClearBasketUseCaseTest {
    private val basketRepository = mockk<BasketRepository>()
    private val useCase = ClearBasketUseCase(basketRepository)

    @Test
    fun `should clear basket with items`() = runTest {
        val basket = basket {
            add(productId(), Quantity.ONE)
            add(productId(), 2.qty())
        }

        coEvery { basketRepository.getOrEmpty() } returns basket
        coEvery { basketRepository.save(any()) } just runs

        useCase.execute()

        coVerify(exactly = 1) {
            basketRepository.save(match { it.isEmpty() })
        }
    }
}
