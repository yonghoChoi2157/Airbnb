package fastcampus.aop.part3.chapter07.data.source

import fastcampus.aop.part3.chapter07.api.HouseDto
import fastcampus.aop.part3.chapter07.api.HouseService
import fastcampus.aop.part3.chapter07.util.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSourceImpl : RemoteDataSource {

    private val service = RetrofitClient.create<HouseService>(HouseService.baseUrl)

    override fun getHouseList(
        onSuccess: (HouseDto) -> Unit,
        onFailure: (String) -> Unit) {

        service.getHouseList().enqueue(
            object : Callback<HouseDto> {
                override fun onResponse(call: Call<HouseDto>, response: Response<HouseDto>) {
                    response.body()?.let(onSuccess)
                }

                override fun onFailure(call: Call<HouseDto>, t: Throwable) {
                    onFailure.invoke(t.message ?: "Error")
                }

            }
        )

    }
}