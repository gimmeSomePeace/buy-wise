package me.gimmesomepeace.buywise.product

class Product(
    val id: ProductId,
    name: String,
) {
    var name: String = name
        private set(value) {
            require(name.isNotBlank()) {
                "Name of product must not be blank"
            }
            field = value
        }

    fun rename(newName: String) {
        name = newName
    }
}