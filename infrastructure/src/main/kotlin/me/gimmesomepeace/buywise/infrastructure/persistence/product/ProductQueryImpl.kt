package me.gimmesomepeace.buywise.infrastructure.persistence.product

import me.gimmesomepeace.buywise.application.product.ProductDetails
import me.gimmesomepeace.buywise.application.product.ProductListItem
import me.gimmesomepeace.buywise.application.product.ProductQuery
import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.user.UserId
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import java.util.*

class ProductQueryImpl(
    private val repository: ProductJpaRepository,
) : ProductQuery {
    override suspend fun find(
        id: ProductId,
    ): ProductDetails? = repository.findByIdOrNull(id.value)?.toDetails()

    override suspend fun list(
        ownerId: UserId,
        request: PageRequest,
    ): Page<ProductListItem> {
        val requestWithExtra = Pageable.ofSize(request.pageSize + 1)

        val entities =
            request.cursor
                ?.let { cursor ->
                    repository.findByOwnerIdAndIdGreaterThanOrderByIdAsc(
                        ownerId = ownerId.value,
                        id = UUID.fromString(cursor.value),
                        pageable = requestWithExtra,
                    )
                }
                ?: repository.findByOwnerId(ownerId.value, requestWithExtra)

        val hasExtra = entities.size > request.pageSize
        val pageItems = if (hasExtra) entities.dropLast(1) else entities

        return Page(
            items = pageItems.map { it.toListItem() },
            cursor =
                if (hasExtra) {
                    Cursor(
                        pageItems.last().id.toString(),
                    )
                } else {
                    null
                },
        )
    }
}
