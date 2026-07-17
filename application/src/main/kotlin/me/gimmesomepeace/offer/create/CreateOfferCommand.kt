package me.gimmesomepeace.offer.create

import me.gimmesomepeace.buywise.product.ProductId
import me.gimmesomepeace.buywise.shared.Money
import me.gimmesomepeace.buywise.store.StoreId

data class CreateOfferCommand(
    val productId: ProductId,
    val storeId: StoreId,
    val unitPrice: Money,
)
