package me.gimmesomepeace.buywise.store

/**
 * Магазин, в котором покупатель может приобрести то, что он предлагает.
 *
 * Инварианты:
 *   - Название магазина не должно быть пустым.
 *
 * @property id Идентификатор магазина.
 * @property name Наименование магазина.
 */
class Store(
    val id: StoreId,
    name: String,
) {
    var name: String = name
        private set(value) {
            require(name.isNotBlank()) { "Name of store must not be blank" }
            field = value
        }

    fun rename(newName: String) {
        name = newName
    }
}
