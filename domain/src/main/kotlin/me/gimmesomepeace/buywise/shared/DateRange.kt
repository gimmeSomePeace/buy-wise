package me.gimmesomepeace.buywise.shared

import java.time.LocalDate

data class DateRange(
    val start: LocalDate,
    val end: LocalDate,
) {
    init {
        require(start <= end) { "start must be <= end" }
    }

    operator fun contains(date: LocalDate): Boolean =
        !date.isBefore(start) && date.isBefore(end)

    fun overlaps(other: DateRange): Boolean =
        start < other.end && other.start < end
}