package fastcampus.aop.part3.chapter07.ui

import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import fastcampus.aop.part3.chapter07.api.HouseDto

sealed class MainViewState {
    data class GetHouseList(val dto: HouseDto) : MainViewState()
    data class Error(val message: String) : MainViewState()
    data class GetMarkerList(val list: List<Marker>) : MainViewState()
    data class ClickOverlay(val overlay: Overlay) : MainViewState()
}
