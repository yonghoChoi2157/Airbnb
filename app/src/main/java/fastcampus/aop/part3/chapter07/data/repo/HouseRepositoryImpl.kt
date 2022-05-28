package fastcampus.aop.part3.chapter07.data.repo

import fastcampus.aop.part3.chapter07.api.HouseDto
import fastcampus.aop.part3.chapter07.data.source.HouseRemoteDataSource
import fastcampus.aop.part3.chapter07.data.source.HouseRemoteDataSourceImpl
import fastcampus.aop.part3.chapter07.util.Result
import javax.inject.Inject

class HouseRepositoryImpl @Inject constructor(private val dataSource: HouseRemoteDataSource) :
    HouseRepository {

    override fun getHouseList(): Result<HouseDto> {
        return dataSource.getHouseList()
    }

}