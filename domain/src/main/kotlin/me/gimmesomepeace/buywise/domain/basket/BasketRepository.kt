package me.gimmesomepeace.buywise.domain.basket

interface BasketRepository {
    /**
     * Возвращает текущую корзину или `null`, если она отсутствует.
     */
    suspend fun find(): Basket?

    // TODO: нужно проверять, что все продукты существуют и id не выдуманные.
    /**
     * Сохраняет текущее состояние корзины.
     *
     * Повторный вызов заменяет ранее сохраненную корзину.
     */
    suspend fun save(
        basket: Basket,
    )
}
