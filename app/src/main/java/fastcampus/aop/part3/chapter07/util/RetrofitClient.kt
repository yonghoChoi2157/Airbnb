package fastcampus.aop.part3.chapter07.util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    inline fun <reified T>create(baseUrl: String): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(T::class.java)
    }
}