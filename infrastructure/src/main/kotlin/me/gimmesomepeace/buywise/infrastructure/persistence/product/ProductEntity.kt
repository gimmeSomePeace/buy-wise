package me.gimmesomepeace.buywise.infrastructure.persistence.product

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "products")
class ProductEntity(
    @Id
    var id: UUID,

    @Column(nullable = false)
    var name: String,
)
