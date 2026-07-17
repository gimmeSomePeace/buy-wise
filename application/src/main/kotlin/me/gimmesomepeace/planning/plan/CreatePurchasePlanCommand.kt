package me.gimmesomepeace.planning.plan

import me.gimmesomepeace.buywise.planning.StoreCountLimit

data class CreatePurchasePlanCommand(
    val storeCountLimit: StoreCountLimit
)
