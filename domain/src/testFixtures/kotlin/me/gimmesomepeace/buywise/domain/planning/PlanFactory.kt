package me.gimmesomepeace.buywise.domain.planning

import me.gimmesomepeace.buywise.domain.planning.plan.PurchaseItem
import me.gimmesomepeace.buywise.domain.planning.plan.PurchasePlan
import me.gimmesomepeace.buywise.domain.planning.plan.StorePurchasePlan
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.shared.Quantity
import me.gimmesomepeace.buywise.domain.shared.qty
import me.gimmesomepeace.buywise.domain.shared.usd
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.storeId

fun plan(vararg items: StorePurchasePlan) = PurchasePlan(items.toList())

fun storePlan(
    storeId: StoreId = storeId(),
    vararg purchases: PurchaseItem,
) = StorePurchasePlan(
    storeId,
    purchases.toList(),
)

fun purchase(
    productId: ProductId = productId(),
    quantity: Quantity = 1.qty(),
    unitPrice: Money = 1.usd(),
) = PurchaseItem(
    productId = productId,
    quantity = quantity,
    unitPrice = unitPrice,
)
