package me.gimmesomepeace.buywise.web.product

import me.gimmesomepeace.buywise.web.product.create.CreateProductRequest

internal fun createProductRequest(name: String = "TEST PRODUCT") =
    CreateProductRequest(
        name = name,
    )
