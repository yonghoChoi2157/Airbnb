package fastcampus.aop.part3.chapter07.data.repo

import fastcampus.aop.part3.chapter07.api.HouseDto

interface HouseRepository {

    fun getHouseList(
        onSuccess: (HouseDto) -> Unit,
        onFailure: (String) -> Unit
    )

}