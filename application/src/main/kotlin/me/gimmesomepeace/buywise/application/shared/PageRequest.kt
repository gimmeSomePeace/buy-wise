package me.gimmesomepeace.buywise.application.shared

data class PageRequest(
    val pageSize: Int,
    val cursor: Cursor? = null,
) {
    init {
        require(pageSize > 0) {
            "Requested page size must be greater than 0. Got: $pageSize"
        }
    }
}
