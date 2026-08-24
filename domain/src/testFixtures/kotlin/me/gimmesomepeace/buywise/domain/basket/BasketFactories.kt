package me.gimmesomepeace.buywise.domain.basket

fun basket(init: Basket.() -> Unit = {}) = Basket().apply(init)
