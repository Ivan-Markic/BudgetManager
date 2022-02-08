package hr.markic.budgetmanager.model

import java.time.LocalDateTime

data class Bill(
    var billID:Long,
    var amount:Double,
    var description:String,
    var time:LocalDateTime,
    var username:String)
