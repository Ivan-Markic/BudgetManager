package hr.markic.budgetmanager.model

import java.time.LocalDateTime

data class Bill(var userToken:String, var category:String,  var amount:Double, var time:LocalDateTime, var description:String, var xOs:Double, var yOs:Double)
