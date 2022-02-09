package hr.markic.budgetmanager.model

data class Item(
    var _id: Long?,
    val title: String,
    val price: Double,
    val description: String,
    val picturePath: String,
    val category: String,
    var read: Boolean
)