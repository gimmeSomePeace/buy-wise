package me.gimmesomepeace.buywise.domain.store

interface StoreRepository {
    /**
     * Возвращает магазин по его идентификатору.
     *
     * @throws StoreException.NotFound если магазин с указанным идентификатором не существует.
     */
    suspend fun get(storeId: StoreId): Store

    /**
     * Сохраняет переданный магазин, воспринимая его как новый.
     *
     * @throws StoreException.AlreadyExists если магазин с таким
     * идентификатором уже существует.
     */
    suspend fun add(store: Store)

    /**
     * Сохраняет изменения существующего магазина.
     *
     * @throws StoreException.NotFound если магазин не существует.
     */
    suspend fun update(store: Store)

    /**
     * Удаляет магазин по указанному идентификатору.
     *
     * @throws StoreException.NotFound если магазин не существует.
     */
    suspend fun delete(id: StoreId)
}
