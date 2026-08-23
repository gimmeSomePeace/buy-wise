package me.gimmesomepeace.buywise.application.product

import me.gimmesomepeace.buywise.domain.user.UserId

data class ProductFilters(
    val ownerId: UserId? = null,
    val nameContains: String? = null,
) {
    init {
        require(nameContains == null || nameContains.isNotBlank()) {
            "Name contains must be null or not blank"
        }
    }
}
