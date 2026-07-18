package me.gimmesomepeace.buywise.domain.planning.offer

import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.store.StoreId

/**
 * Каталог предложений.
 *
 * Представляет совокупность предложений, доступных для приобретения продуктов.
 *
 * Инварианты:
 *   - Для каждого продукта не может существовать более одного предложения от одного и того же магазина.
 */
class AvailableOfferCatalog(
    offers: Iterable<AvailableOffer>,
) {
    private val offers =
        offers
            .groupBy { it.productId }
            .mapValues { (_, offers) ->
                require(offers.size == offers.distinctBy { it.storeId }.size) {
                    "Duplicate offers are not allowed."
                }

                offers.associateBy { it.storeId }
            }

    /**
     * Возвращает идентификаторы магазинов, имеющих хотя бы одно предложение.
     */
    fun stores(): Set<StoreId> = offers.values.flatMap { it.keys }.toSet()

    /**
     * Возвращает все предложения от магазинов для определенного товара.
     *
     * @param productId Идентификатор продукта, предложения на который будут возвращены.
     */
    fun forProduct(productId: ProductId): List<AvailableOffer> = offers[productId]?.values?.toList() ?: emptyList()

    /**
     * Возвращает предложение указанного магазина на указанный товар.
     *
     * @param productId Идентификатор продукта, предложение на который будет возвращено.
     * @param storeId Идентификатор магазина, от которого и будет возвращено предложение.
     * @return предложение или `null`, если оно отсутствует.
     */
    fun of(
        productId: ProductId,
        storeId: StoreId,
    ): AvailableOffer? = offers[productId]?.get(storeId)
}
