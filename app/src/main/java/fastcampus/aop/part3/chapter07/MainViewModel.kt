package fastcampus.aop.part3.chapter07

    import android.content.Context
    import android.graphics.Color
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
    import com.naver.maps.geometry.LatLng
    import com.naver.maps.map.NaverMap
    import com.naver.maps.map.overlay.Marker
    import com.naver.maps.map.util.MarkerIcons
    import fastcampus.aop.part3.chapter07.api.HouseModel
    import fastcampus.aop.part3.chapter07.data.repo.Repository
    import fastcampus.aop.part3.chapter07.ui.activity.MainActivity

class MainViewModel(private val repository: Repository): ViewModel() {

    private val _mainViewStateLiveData = MutableLiveData<MainViewState>()
    val mainViewStateLiveData: LiveData<MainViewState>
        get() = _mainViewStateLiveData


    fun requestHouseList() {
        repository.getHouseList(
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