package hr.markic.budgetmanager.dao

import android.content.Context

fun getProductRepository(context: Context?) = ProductSqlHelper(context)