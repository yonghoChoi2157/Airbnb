package fastcampus.aop.part3.chapter07.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fastcampus.aop.part3.chapter07.data.repo.HouseRepository
import fastcampus.aop.part3.chapter07.data.repo.HouseRepositoryImpl
import fastcampus.aop.part3.chapter07.data.source.HouseRemoteDataSource
import fastcampus.aop.part3.chapter07.data.source.HouseRemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    @Singleton
    abstract fun bindHouseRepository(houseRepositoryImpl: HouseRepositoryImpl): HouseRepository


    @Binds
    @Singleton
    abstract fun bindHouseRemoteDataSource(houseRemoteDataSourceImpl: HouseRemoteDataSourceImpl): HouseRemoteDataSource

}