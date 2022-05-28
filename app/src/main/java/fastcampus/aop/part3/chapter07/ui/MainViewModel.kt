package fastcampus.aop.part3.chapter07.ui

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.lifecycle.HiltViewModel
import fastcampus.aop.part3.chapter07.api.HouseModel
import fastcampus.aop.part3.chapter07.data.repo.HouseRepository
import fastcampus.aop.part3.chapter07.util.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val houseRepository: HouseRepository) :
    ViewModel() {

    private val _mainViewStateLiveData = MutableLiveData<MainViewState>()
    val mainViewStateLiveData: LiveData<MainViewState>
        get() = _mainViewStateLiveData

    fun requestHouseList() {
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = houseRepository.getHouseList()) {
                is Result.Success -> {
                    if (result.data.items.isNotEmpty()) {
                        onChangeViewState(MainViewState.GetMarkerList(updateMarker(result.data.items)))
                        onChangeViewState(MainViewState.GetHouseList(result.data))
                    } else {
                        onChangeViewState(MainViewState.Error("데이터가 없습니다."))
                    }
                }

                is Result.Error -> {
                    onChangeViewState(
                        MainViewState.Error(
                            result.exception.message ?: "데이터를 가져오지 못했습니다."
                        )
                    )
                }
            }
        }
    }

    private suspend fun onChangeViewState(viewState: MainViewState) =
        withContext(Dispatchers.Main) {
            _mainViewStateLiveData.value = viewState
        }


    private fun updateMarker(houses: List<HouseModel>): List<Marker> {
        return houses.map { house ->
            Marker().apply {
                position = LatLng(house.lat, house.lng)
                tag = house.id
                icon = MarkerIcons.BLACK
                iconTintColor = Color.RED
            }
        }
    }

}