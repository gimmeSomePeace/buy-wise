package me.gimmesomepeace.buywise.domain.offer

import me.gimmesomepeace.buywise.domain.planning.offer.AvailableOfferCatalog

interface OfferRepository {
    /**
     * Возвращает предложение по его идентификатору.
     *
     * @throws OfferException.NotFound если предложение с указанным идентификатором не существует.
     */
    suspend fun get(offerId: OfferId): Offer

    /**
     * Сохраняет переданное предложение, воспринимая его как новое.
     *
     * @throws OfferException.AlreadyExists если предложение с таким
     * идентификатором уже существует.
     */
    suspend fun add(offer: Offer)

    /**
     * Сохраняет изменения существующего предложения.
     *
     * @throws OfferException.NotFound если предложение не существует.
     */
    suspend fun update(offer: Offer)

    /**
     * Удаляет предложение по указанному идентификатору.
     *
     * @throws OfferException.NotFound если предложение не существует.
     */
    suspend fun delete(offerId: OfferId)

    /**
     * Возвращает каталог предложений, действующих на текущий момент.
     */
    suspend fun availableOffers(): AvailableOfferCatalog
}
