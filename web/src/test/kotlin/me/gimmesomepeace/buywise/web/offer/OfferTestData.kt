package me.gimmesomepeace.buywise.web.offer

import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.store.storeId
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

@Suppress("unused")
object OfferTestData {

    @JvmStatic
    fun invalidCreateOfferRequests(): Stream<Arguments> = Stream.of(
        Arguments.of(createOfferRequestMap(productId = "not-uuid")),
        Arguments.of(createOfferRequestMap(storeId = "not-uuid")),
        Arguments.of(createOfferRequestMap().minus("productId")),
        Arguments.of(createOfferRequestMap().minus("storeId")),
        Arguments.of(createOfferRequestMap(unitPrice = "-5")),
        Arguments.of(createOfferRequestMap(unitPrice = "0")),
    )

    private fun createOfferRequestMap(
        productId: String = productId().value.toString(),
        storeId: String = storeId().value.toString(),
        unitPrice: String = "5",
        currency: String = "USD",
    ): Map<String, Any?> = mapOf(
        "productId" to productId,
        "storeId" to storeId,
        "unitPrice" to unitPrice,
        "currency" to currency,
    )
}
