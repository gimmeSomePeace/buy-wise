package me.gimmesomepeace.buywise.application.product

import me.gimmesomepeace.buywise.domain.product.Product

suspend fun productRepository(vararg products: Product) =
    InMemoryProductRepository().apply {
        products.forEach {
            add(it)
        }
    }
