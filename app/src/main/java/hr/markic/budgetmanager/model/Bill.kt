package hr.markic.budgetmanager.model

import java.time.LocalDateTime

data class Bill(
    var billID:Long,
    var title: String,
    var amount:Double,
    var time:LocalDateTime,
    var username:String)
