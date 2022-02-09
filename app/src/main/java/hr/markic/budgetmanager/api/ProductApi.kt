package hr.markic.budgetmanager.api

import retrofit2.Call
import retrofit2.http.GET

const val API_URL = "https://fakestoreapi.com/"
interface NasaApi {
    @GET("products?limit=20")
    fun fetchItems() : Call<List<ProductItem>>
}