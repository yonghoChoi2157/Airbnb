package fastcampus.aop.part3.chapter07.data.source

import com.nhaarman.mockitokotlin2.mock
import fastcampus.aop.part3.chapter07.api.HouseDto
import fastcampus.aop.part3.chapter07.api.HouseModel
import fastcampus.aop.part3.chapter07.api.HouseService
import fastcampus.aop.part3.chapter07.util.Result
import okhttp3.Request
import okio.Timeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HouseRemoteDataSourceImplTest {


    private lateinit var houseRemoteDataSource: HouseRemoteDataSource

    private val houseService: HouseService = mock()

    @Before
    fun setUp() {
        initMockHouseApi()
        houseRemoteDataSource = HouseRemoteDataSourceImpl(houseService)
    }


    @Test
    fun getHouseListSuccessTest() {

        assertEquals((houseRemoteDataSource.getHouseList() as Result.Success).data, mockHouseDto)

    }

    @Test
    fun getHouseListFailureTest() {
        val failResult = Result.Error(Exception("에러가 발생."))

        Mockito.`when`(houseService.getHouseList()).then { failResult }

        assertEquals((houseRemoteDataSource.getHouseList() as Result.Error).exception.message, failResult.exception.message)
    }

    @After
    fun tearDown() {

    }


    private fun initMockHouseApi() {

        val call = object : Call<HouseDto> {
            override fun execute(): Response<HouseDto> {
                return Response.success(mockHouseDto)
            }

            override fun clone(): Call<HouseDto> {
                TODO("Not yet implemented")
            }

            override fun enqueue(callback: Callback<HouseDto>) {
                TODO("Not yet implemented")
            }

            override fun isExecuted(): Boolean {
                TODO("Not yet implemented")
            }

            override fun cancel() {
                TODO("Not yet implemented")
            }

            override fun isCanceled(): Boolean {
                TODO("Not yet implemented")
            }

            override fun request(): Request {
                TODO("Not yet implemented")
            }

            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }
        }

        Mockito.`when`(houseService.getHouseList()).thenReturn(call)
    }

    companion object {
        val mockHouseDto = HouseDto(
            listOf(
                HouseModel(
                    id = 1,
                    title = "Test",
                    price = "",
                    imgUrl = "",
                    lat = 0.0,
                    lng = 0.0
                )
            )
        )
    }
}