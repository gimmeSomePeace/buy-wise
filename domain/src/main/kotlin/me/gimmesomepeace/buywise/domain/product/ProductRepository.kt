package me.gimmesomepeace.buywise.domain.product

/**
 * Репозиторий продуктов.
 */
interface ProductRepository {
    /**
     * Возвращает продукт по его идентификатору.
     *
     * @throws ProductException.NotFound если товар с указанным идентификатором не существует.
     */
    suspend fun get(productId: ProductId): Product

    /**
     * Сохраняет переданный продукт, воспринимая его как новый.
     *
     * @throws ProductException.AlreadyExists если продукт с таким идентификатором уже существует.
     */
    suspend fun add(product: Product)

    /**
     * Сохраняет изменения существующего продукта.
     *
     * @throws ProductException.NotFound если продукт не существует.
     */
    suspend fun update(product: Product)

    /**
     * Удаляет продукт по указанному идентификатору.
     *
     * @throws ProductException.NotFound если продукт не существует.
     */
    suspend fun delete(productId: ProductId)
}
