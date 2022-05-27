package fastcampus.aop.part3.chapter07.data.source

import fastcampus.aop.part3.chapter07.api.HouseDto

interface RemoteDataSource {

    fun getHouseList(
        onSuccess: (HouseDto) -> Unit,
        onFailure: (String) -> Unit
    )

}