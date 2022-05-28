package fastcampus.aop.part3.chapter07.data.source

import fastcampus.aop.part3.chapter07.api.HouseDto
import fastcampus.aop.part3.chapter07.api.HouseService
import fastcampus.aop.part3.chapter07.util.Result
import javax.inject.Inject


class HouseRemoteDataSourceImpl @Inject constructor(private val houseService: HouseService) :
    HouseRemoteDataSource {
    override fun getHouseList(): Result<HouseDto> {
        return try {
            val response = houseService.getHouseList().execute().body()!!
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(Exception("에러가 발생."))
        }
    }
}