package hr.markic.budgetmanager

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import hr.markic.budgetmanager.api.ProductFetcher

private const val JOB_ID = 1
class ProductService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {

        ProductFetcher(this).fetchItems()
    }

    companion object {
        fun enqueue(context: Context, intent: Intent) {
            enqueueWork(context, ProductService::class.java, JOB_ID, intent)
        }
    }
}