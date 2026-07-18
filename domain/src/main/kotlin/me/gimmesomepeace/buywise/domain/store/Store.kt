package me.gimmesomepeace.buywise.domain.store

/**
 * Магазин, в котором покупатель может приобрести то, что он предлагает.
 *
 * Инварианты:
 *   - Название магазина не должно быть пустым.
 *
 * @property id Идентификатор магазина.
 */
class Store(
    val id: StoreId,
    name: String,
) {
    /**
     * Наименование магазина.
     */
    var name: String = name
        private set(value) {
            require(value.isNotBlank()) { "Name of store must not be blank" }
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
