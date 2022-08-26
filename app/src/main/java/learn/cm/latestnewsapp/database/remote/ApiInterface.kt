package learn.cm.latestnewsapp.database.remote

import learn.cm.latestnewsapp.database.remote.model.NewsResponse
import learn.cm.latestnewsapp.util.GenericProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiInterface {

    @GET("/geolocation")
    fun getNews(
        @Query("geo") geo: String,
        @Query("country") country: String,
        @Query("lang") lang: String = "en"
    ) : Call<NewsResponse>

    companion object {
        var BASE_URL = "https://google-news1.p.rapidapi.com"

        private val client = OkHttpClient.Builder().apply {
            addInterceptor(MyInterceptor())
        }.build()

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }

        class MyInterceptor: okhttp3.Interceptor{
            override fun intercept(chain: okhttp3.Interceptor.Chain): Response {
                val req = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type","application/json")
                    .addHeader("X-RapidAPI-Key",GenericProvider.getRapidApiKey())
                    .build()

                return chain.proceed(req)
            }

        }
    }
}