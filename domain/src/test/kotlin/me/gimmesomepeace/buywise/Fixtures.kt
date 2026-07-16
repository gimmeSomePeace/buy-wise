package me.gimmesomepeace.buywise

import me.gimmesomepeace.buywise.offer.Offer
import me.gimmesomepeace.buywise.offer.OffersCatalog
import me.gimmesomepeace.buywise.planning.plan.PurchaseItem
import me.gimmesomepeace.buywise.planning.plan.PurchasePlan
import me.gimmesomepeace.buywise.planning.plan.StorePurchasePlan
import me.gimmesomepeace.buywise.product.ProductId
import me.gimmesomepeace.buywise.shared.Currency
import me.gimmesomepeace.buywise.shared.Money
import me.gimmesomepeace.buywise.shared.Quantity
import me.gimmesomepeace.buywise.store.StoreId
import java.math.BigDecimal
import java.util.UUID

fun productId() = ProductId(UUID.randomUUID())

fun storeId() = StoreId(UUID.randomUUID())

fun money(
    value: Long,
    currency: Currency = Currency.USD,
) = Money(BigDecimal.valueOf(value), currency)

fun offer(
    productId: ProductId,
    storeId: StoreId,
    price: Long,
) = Offer(
    storeId = storeId,
    productId = productId,
    unitPrice = money(price),
)

fun offersCatalog(vararg offers: Offer) = OffersCatalog(offers.toList())

fun plan(vararg items: StorePurchasePlan) = PurchasePlan(items.toList())

fun storePlan(
    storeId: StoreId,
    vararg purchases: PurchaseItem,
) = StorePurchasePlan(storeId, purchases.toList())

fun purchase(
    productId: ProductId,
    quantity: Int,
    unitPrice: Long,
) = PurchaseItem(
    productId = productId,
    quantity = Quantity(quantity),
    unitPrice = money(unitPrice),
)
