package fastcampus.aop.part3.chapter07.data.repo

import fastcampus.aop.part3.chapter07.api.HouseDto
import fastcampus.aop.part3.chapter07.data.source.RemoteDataSourceImpl

class RepositoryImpl(private val dataSourceImpl: RemoteDataSourceImpl) : Repository {
    override fun getHouseList(onSuccess: (HouseDto) -> Unit, onFailure: (String) -> Unit) {
        dataSourceImpl.getHouseList(onSuccess, onFailure)
    }
}