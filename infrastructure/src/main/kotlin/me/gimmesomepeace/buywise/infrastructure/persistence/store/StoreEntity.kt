package me.gimmesomepeace.buywise.infrastructure.persistence.store

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "stores")
class StoreEntity(
    @Id
    var id: UUID,

    @Column(nullable = false)
    var ownerId: UUID,

    @Column(nullable = false)
    var name: String,
)
