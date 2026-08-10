package me.gimmesomepeace.buywise.application.basket

interface BasketQuery {
    suspend fun find(): BasketDetails?
}
