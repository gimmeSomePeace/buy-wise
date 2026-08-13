package me.gimmesomepeace.buywise.infrastructure.persistence.offer

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import me.gimmesomepeace.buywise.domain.shared.Currency
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "offers")
class OfferEntity(
    @Id
    var id: UUID,

    @Column(nullable = false)
    var productId: UUID,
    @Column(nullable = false)
    var storeId: UUID,
    @Column(nullable = false)
    var price: BigDecimal,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var currency: Currency,
)
