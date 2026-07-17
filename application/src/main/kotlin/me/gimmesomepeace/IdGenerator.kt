package me.gimmesomepeace

/**
 * Генератор идентификаторов.
 *
 * @param T Тип идентификатора.
 */
fun interface IdGenerator<T> {
    /**
     * Генерирует новый уникальный идентификатор.
     *
     * @return Сгенерированный идентификатор.
     */
    fun generate(): T
}
