package hr.markic.budgetmanager.api

import retrofit2.Call
import retrofit2.http.GET

const val API_URL = "https://api.nasa.gov/planetary/"
interface NasaApi {
    @GET("apod?api_key=DEMO_KEY&count=25")
    fun fetchItems() : Call<List<NasaItem>>
}