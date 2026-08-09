package me.gimmesomepeace.buywise.application.shared

data class Page<T>(
    val items: List<T>,
    val cursor: Cursor?
)
