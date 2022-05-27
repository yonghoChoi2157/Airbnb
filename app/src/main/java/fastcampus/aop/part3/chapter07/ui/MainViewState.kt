package fastcampus.aop.part3.chapter07.ui

import fastcampus.aop.part3.chapter07.api.HouseDto

sealed class MainViewState {
    data class GetHouseList(val dto: HouseDto) : MainViewState()
    data class Error(val message: String) : MainViewState()
}
