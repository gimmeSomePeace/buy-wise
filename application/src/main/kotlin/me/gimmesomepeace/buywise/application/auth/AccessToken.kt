package me.gimmesomepeace.buywise.application.auth

@JvmInline
value class AccessToken(
    val value: String,
) {
    init {
        require(value.isNotBlank()) {
            "The access token must not be blank"
        }
    }
}
