package me.gimmesomepeace.buywise.basket

import me.gimmesomepeace.buywise.product.ProductId
import me.gimmesomepeace.buywise.shared.Quantity

/**
 * Корзина покупателя.
 *
 * Содержит выбранные для покупки продукты и их количество.
 *
 * Инварианты:
 *  - Каждый продукт может присутствовать не более одного раза.
 *  - Количество каждого продукта всегда положительно.
 */
class Basket {
    private val items = mutableMapOf<ProductId, Quantity>()

    /**
     * Добавляет указанное количество товара в корзину.
     * Если продукт уже присутствует, то его количество просто увеличивается.
     *
     * @param productId Идентификатор товара, который нужно добавить в корзину.
     * @param quantity Количество товара для добавления.
     */
    fun add(
        productId: ProductId,
        quantity: Quantity,
    ) {
        items[productId] = (items[productId] ?: Quantity.ZERO) + quantity
    }

    /**
     * Возвращает количество переданного товара в корзине.
     *
     * @param productId Идентификатор товара.
     * @return Количество товара в корзине или [Quantity.ZERO], если продукт отсутствует.
     */
    fun quantityOf(productId: ProductId): Quantity = items[productId] ?: Quantity.ZERO

    /**
     * Уменьшает количество товара в корзине на указанную величину.
     * Если количество товара после выполнения операции стало равно нулю, то продукт удаляется из корзины.
     *
     * @param productId Идентификатор товара, количество которого необходимо уменьшить.
     * @param quantity Величина, на которую необходимо уменьшить количество товара в корзине.
     *
     * @throws IllegalArgumentException если величина, на которую
     * нужно уменьшить кол-во товара в корзине не является положительной.
     * @throws BasketException.ProductNotInBasket если продукт отсутствует в корзине.
     */
    fun decrease(
        productId: ProductId,
        quantity: Quantity,
    ) {
        require(quantity.isPositive()) { "Quantity to decrease must be positive" }

        val currentQuantity =
            items[productId]
                ?: throw BasketException.ProductNotInBasket(productId)

        if (quantity >= currentQuantity) {
            items.remove(productId)
        } else {
            items[productId] = currentQuantity - quantity
        }
    }

    /**
     * Возвращает все позиции корзины.
     */
    fun items(): List<BasketItem> = items.map { (productId, quantity) -> BasketItem(productId, quantity) }

    /**
     * Удаляет указанный продукт из корзины.
     *
     * @param productId Идентификатор товара, который нужно удалить из корзины.
     */
    fun remove(productId: ProductId) {
        items.remove(productId)
    }

    /**
     * Очищает корзину.
     */
    fun clear() {
        items.clear()
    }

    /**
     * Проверяет, пуста ли корзина.
     */
    fun isEmpty() = items.isEmpty()
}
