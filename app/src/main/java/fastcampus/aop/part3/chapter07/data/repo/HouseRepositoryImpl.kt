package fastcampus.aop.part3.chapter07.data.repo

import fastcampus.aop.part3.chapter07.api.HouseDto
import fastcampus.aop.part3.chapter07.data.source.HouseRemoteDataSourceImpl
import javax.inject.Inject

class HouseRepositoryImpl @Inject constructor(private val dataSourceImpl: HouseRemoteDataSourceImpl) :
    HouseRepository {
    override fun getHouseList(onSuccess: (HouseDto) -> Unit, onFailure: (String) -> Unit) {
        dataSourceImpl.getHouseList(onSuccess, onFailure)
    }
}