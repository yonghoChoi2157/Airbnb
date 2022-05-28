package fastcampus.aop.part3.chapter07.data.repo

import fastcampus.aop.part3.chapter07.api.HouseDto
import fastcampus.aop.part3.chapter07.util.Result

interface HouseRepository {

    fun getHouseList(): Result<HouseDto>

}