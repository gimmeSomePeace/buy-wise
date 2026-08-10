package me.gimmesomepeace.buywise.infrastructure.persistence.basket

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.validation.constraints.Positive
import java.util.UUID

@Entity(name = "basket")
class BasketEntity(
    @Id
    var productId: UUID,
    @Column(nullable = false)
    @field:Positive
    var quantity: Int,
)
