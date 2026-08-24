package me.gimmesomepeace.buywise.infrastructure.persistence.product.specification

import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.infrastructure.persistence.product.ProductEntity
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

object ProductSpecifications {
    fun byOwner(ownerId: UserId): Specification<ProductEntity> =
        Specification { root, _, builder ->
            builder.equal(
                root.get<UUID>("ownerId"),
                ownerId.value,
            )
        }

    fun nameContains(pattern: String): Specification<ProductEntity> =
        Specification { root, _, builder ->
            builder.like(
                builder.lower(root.get("name")),
                "%${pattern.lowercase()}%",
            )
        }

    fun afterCursor(cursor: Cursor): Specification<ProductEntity> =
        Specification { root, _, builder ->
            builder.greaterThan(
                root.get("id"),
                UUID.fromString(cursor.value),
            )
        }
}
