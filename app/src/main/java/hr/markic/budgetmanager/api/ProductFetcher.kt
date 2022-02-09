package hr.markic.budgetmanager.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.markic.budgetmanager.DATA_IMPORTED
import hr.markic.budgetmanager.PRODUCT_PROVIDER_URI
import hr.markic.budgetmanager.ProductReceiver
import hr.markic.budgetmanager.framework.downloadImageAndStore
import hr.markic.budgetmanager.framework.sendBroadcast
import hr.markic.budgetmanager.framework.setBooleanPreference
import hr.markic.budgetmanager.model.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductFetcher(private val context: Context) {
    private var nasaApi: NasaApi
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        nasaApi = retrofit.create(NasaApi::class.java)
    }

    fun fetchItems() {
        val request = nasaApi.fetchItems()

        request.enqueue(object: Callback<List<ProductItem>> {
            override fun onResponse(
                call: Call<List<ProductItem>>,
                response: Response<List<ProductItem>>
            ) {
                response.body()?.let {
                    populateItems(it)
                }
            }

            override fun onFailure(call: Call<List<ProductItem>>, t: Throwable) {
                Log.e(javaClass.name, t.message, t)
            }

        })
    }

    private fun populateItems(nasaItems: List<ProductItem>) {
        GlobalScope.launch {
            nasaItems.forEach {

                var fileName = it.title.replace(" ", "_")
                fileName = fileName.replace("/", "_")
                val pero = fileName

                var picturePath = downloadImageAndStore(
                    context,
                    it.imageUrl,
                    fileName)

                val values = ContentValues().apply {
                    put(Item::title.name, it.title)
                    put(Item::price.name, it.price)
                    put(Item::description.name, it.description)
                    put(Item::picturePath.name, picturePath ?: "")
                    put(Item::category.name, it.category)
                    put(Item::read.name, false)
                }
                context.contentResolver.insert(PRODUCT_PROVIDER_URI, values)

            }
            context.setBooleanPreference(DATA_IMPORTED, true)
            context.sendBroadcast<ProductReceiver>()
        }
    }


}