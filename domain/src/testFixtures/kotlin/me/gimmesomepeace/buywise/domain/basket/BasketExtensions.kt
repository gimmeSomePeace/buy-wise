package me.gimmesomepeace.buywise.domain.basket

suspend fun BasketRepository.getOrEmpty() = find() ?: Basket()
