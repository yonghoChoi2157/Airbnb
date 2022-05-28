package fastcampus.aop.part3.chapter07.data.repo

import com.nhaarman.mockitokotlin2.mock
import fastcampus.aop.part3.chapter07.data.source.HouseRemoteDataSource
import fastcampus.aop.part3.chapter07.data.source.HouseRemoteDataSourceImplTest.Companion.mockHouseDto
import fastcampus.aop.part3.chapter07.util.Result
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class HouseRepositoryImplTest {

    private lateinit var houseRepository: HouseRepository

    private val houseRemoteDataSource: HouseRemoteDataSource = mock()

    @Before
    fun setUp() {
        houseRepository = HouseRepositoryImpl(houseRemoteDataSource)
    }


    @Test
    fun getHouseListSuccessTest() {

        Mockito.`when`(houseRemoteDataSource.getHouseList()).thenReturn(Result.Success(mockHouseDto))

        Assert.assertEquals(
            (houseRepository.getHouseList() as Result.Success),
            houseRemoteDataSource.getHouseList()
        )

    }


    @Test
    fun getHouseListFailureTest() {
        val failResult = Result.Error(Exception("에러가 발생."))
        Mockito.`when`(houseRemoteDataSource.getHouseList()).then { failResult }

        Assert.assertEquals(
            (houseRepository.getHouseList() as Result.Error),
            houseRemoteDataSource.getHouseList()
        )

    }

    @After
    fun tearDown() {

    }


}