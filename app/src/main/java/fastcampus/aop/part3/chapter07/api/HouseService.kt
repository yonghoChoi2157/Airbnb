package fastcampus.aop.part3.chapter07.api

import retrofit2.Call
import retrofit2.http.GET

interface HouseService {

    companion object {
        const val baseUrl = "https://run.mocky.io"
    }

    @GET("/v3/511c37d3-79c1-455f-9efb-98b5d594e640")
    fun getHouseList(): Call<HouseDto>
}