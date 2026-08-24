package me.gimmesomepeace.buywise.infrastructure.persistence.store.specification

import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.infrastructure.persistence.store.StoreEntity
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

object StoreSpecifications {
    fun byOwner(ownerId: UserId): Specification<StoreEntity> =
        Specification { root, _, builder ->
            builder.equal(
                root.get<UUID>("ownerId"),
                ownerId.value,
            )
        }

    fun nameContains(pattern: String): Specification<StoreEntity> =
        Specification { root, _, builder ->
            builder.like(
                builder.lower(root.get("name")),
                "%${pattern.lowercase()}%",
            )
        }

    fun afterCursor(cursor: Cursor): Specification<StoreEntity> =
        Specification { root, _, builder ->
            builder.greaterThan(
                root.get("id"),
                UUID.fromString(cursor.value),
            )
        }
}
