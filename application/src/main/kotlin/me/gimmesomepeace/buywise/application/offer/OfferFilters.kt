package me.gimmesomepeace.buywise.application.offer

import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Currency
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.user.UserId
import java.math.BigDecimal

private const val MAX_COUNT_IN_LIST = 100

data class OfferFilters(
    val ownerId: UserId? = null,
    val productIds: List<ProductId>? = null,
    val storeIds: List<StoreId>? = null,
    val minPrice: BigDecimal? = null,
    val maxPrice: BigDecimal? = null,
    val currencies: List<Currency>? = null,
) {
    init {
        require(minPrice == null || minPrice >= BigDecimal.ZERO)
            "minPrice must be >= 0, got $minPrice"
        require(maxPrice == null || maxPrice >= BigDecimal.ZERO)
            "maxPrice must be >= 0, got $maxPrice"
        if (minPrice != null && maxPrice != null)
            require(minPrice <= maxPrice)
                "minPrice must be <= maxPrice, got $minPrice <= $maxPrice"

        require(productIds == null || productIds.size <= MAX_COUNT_IN_LIST)
            "productsIds size must be <= $MAX_COUNT_IN_LIST, got ${productIds?.size}"
        require(storeIds == null || storeIds.size <= MAX_COUNT_IN_LIST)
            "storeIds size must be <= $MAX_COUNT_IN_LIST, got ${productIds?.size}"
        require(currencies == null || currencies.size <= MAX_COUNT_IN_LIST)
            "currencies size must be <= $MAX_COUNT_IN_LIST, got ${currencies?.size}"

        require(productIds == null || productIds.isNotEmpty()) {
            "productsIds must be null or not empty"
        }
        require(storeIds == null || storeIds.isNotEmpty()) {
            "storeIds must be null or not empty"
        }
        require(currencies == null || currencies.isNotEmpty()) {
            "currencies must be null or not empty"
        }
    }
}
