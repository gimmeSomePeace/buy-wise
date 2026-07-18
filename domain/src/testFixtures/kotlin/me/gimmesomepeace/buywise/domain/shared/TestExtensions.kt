package me.gimmesomepeace.buywise.domain.shared

fun Int.qty() = Quantity(this)

fun Int.rub() =
    Money(
        this.toBigDecimal(),
        Currency.RUB,
    )

fun Int.usd() =
    Money(
        this.toBigDecimal(),
        Currency.USD,
    )
