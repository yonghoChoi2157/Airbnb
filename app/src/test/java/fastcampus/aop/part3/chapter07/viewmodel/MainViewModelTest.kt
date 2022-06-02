package fastcampus.aop.part3.chapter07.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import fastcampus.aop.part3.chapter07.api.HouseDto
import fastcampus.aop.part3.chapter07.data.repo.HouseRepository
import fastcampus.aop.part3.chapter07.data.source.HouseRemoteDataSourceImplTest.Companion.mockHouseDto
import fastcampus.aop.part3.chapter07.ui.MainViewModel
import fastcampus.aop.part3.chapter07.ui.MainViewState
import fastcampus.aop.part3.chapter07.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var viewStateObserver: Observer<MainViewState>

    private val houseRepository: HouseRepository = mock()

    private lateinit var mainViewModel: MainViewModel


    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        mainViewModel = MainViewModel(houseRepository)
        mainViewModel.mainViewStateLiveData.observeForever(viewStateObserver)
    }

    @Test
    fun checkRequestHouseListSuccessTest() = runBlocking {

        val successHouseList = Result.Success(mockHouseDto)

        Mockito.`when`(houseRepository.getHouseList()).thenReturn(successHouseList)

        mainViewModel.requestHouseList()

        delay(100)

        Mockito.verify(viewStateObserver)
            .onChanged(MainViewState.GetHouseList(successHouseList.data))
    }

    @Test
    fun checkRequestHouseListEmptyTest() = runBlocking {
        val emptyHouseList = Result.Success(HouseDto(emptyList()))

        Mockito.`when`(houseRepository.getHouseList()).thenReturn(emptyHouseList)

        mainViewModel.requestHouseList()

        delay(100)


        Mockito.verify(viewStateObserver)
            .onChanged(MainViewState.Error("데이터가 없습니다."))
    }

    @Test
    fun checkRequestHouseListFailureTest() = runBlocking {

        val failureHouseList = Result.Error(Exception("데이터를 가져오지 못했습니다."))

        Mockito.`when`(houseRepository.getHouseList()).then { failureHouseList }

        mainViewModel.requestHouseList()

        delay(100)

        Mockito.verify(viewStateObserver)
            .onChanged(MainViewState.Error("데이터를 가져오지 못했습니다."))
    }
}