package fastcampus.aop.part3.chapter07.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fastcampus.aop.part3.chapter07.data.repo.HouseRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val houseRepository: HouseRepository) :
    ViewModel() {

    private val _mainViewStateLiveData = MutableLiveData<MainViewState>()
    val mainViewStateLiveData: LiveData<MainViewState>
        get() = _mainViewStateLiveData


    fun requestHouseList() {
        houseRepository.getHouseList(
            onSuccess = { result ->
                if (result.items.isNotEmpty()) {
                    _mainViewStateLiveData.value = MainViewState.GetHouseList(result)
                } else {
                    _mainViewStateLiveData.value = MainViewState.Error("데이터가 없습니다.")
                }
            },
            onFailure = {
                _mainViewStateLiveData.value = MainViewState.Error("데이터를 가져오지 못했습니다.")
            }

        )
    }

}