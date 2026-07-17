package me.gimmesomepeace.buywise.product

interface ProductRepository {

    suspend fun get(productId: ProductId): Product

    suspend fun add(product: Product)

    suspend fun update(product: Product)

    suspend fun delete(productId: ProductId)
}