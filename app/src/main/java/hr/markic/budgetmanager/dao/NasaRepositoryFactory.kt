package hr.markic.budgetmanager.dao

import android.content.Context

fun getNasaRepository(context: Context?) = NasaSqlHelper(context)