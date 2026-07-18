package me.gimmesomepeace.buywise.domain.product

/**
 * Продукт.
 *
 * Инварианты:
 *  - Наименование товара не может быть пустым.
 *
 *  @property id Идентификатор товара.
 */
class Product(
    val id: ProductId,
    name: String,
) {
    /**
     * Наименование товара.
     */
    var name: String = name
        private set(value) {
            require(value.isNotBlank()) {
                "Name of product must not be blank"
            }
            field = value
        }

    init {
        // Setter не вызывается при инициализации свойства, поэтому
        // повторно присваиваем значение для проверки инварианта.
        this.name = name
    }

    fun rename(newName: String) {
        name = newName
    }
}
