package fastcampus.aop.part3.chapter07.data.source

import fastcampus.aop.part3.chapter07.api.HouseDto
import fastcampus.aop.part3.chapter07.util.Result

interface HouseRemoteDataSource {

    fun getHouseList(): Result<HouseDto>

}