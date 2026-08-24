package me.gimmesomepeace.buywise.web.product

import me.gimmesomepeace.buywise.web.product.create.CreateProductRequest
import me.gimmesomepeace.buywise.web.product.rename.RenameProductRequest

internal fun createProductRequest(name: String = "TEST PRODUCT") =
    CreateProductRequest(
        name = name,
    )

internal fun renameProductRequest(name: String = "NEW NAME") =
    RenameProductRequest(
        name = name,
    )
