package fastcampus.aop.part3.chapter07.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import fastcampus.aop.part3.chapter07.MainViewModel
import fastcampus.aop.part3.chapter07.MainViewState
import fastcampus.aop.part3.chapter07.R
import fastcampus.aop.part3.chapter07.api.HouseModel
import fastcampus.aop.part3.chapter07.data.repo.RepositoryImpl
import fastcampus.aop.part3.chapter07.data.source.RemoteDataSourceImpl
import fastcampus.aop.part3.chapter07.databinding.ActivityMainBinding
import fastcampus.aop.part3.chapter07.databinding.BottomSheetBinding
import fastcampus.aop.part3.chapter07.ui.adapter.HouseListAdapter
import fastcampus.aop.part3.chapter07.ui.adapter.HouseViewPagerAdapter

class MainActivity : AppCompatActivity(), OnMapReadyCallback, Overlay.OnClickListener {

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var bottomSheetBinding: BottomSheetBinding
    private val recyclerAdapter by lazy { HouseListAdapter() }
    private val viewPagerAdapter by lazy { HouseViewPagerAdapter() }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bottomSheetBinding = DataBindingUtil.setContentView(this, R.layout.bottom_sheet)
        setContentView(mainBinding.root)
        mainBinding.mapView.onCreate(savedInstanceState)

        initUi()
        initViewModel()

        mainBinding.houseViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val selectedHouseModel = viewPagerAdapter.currentList[position]
                val cameraUpdate =
                    CameraUpdate.scrollTo(LatLng(selectedHouseModel.lat, selectedHouseModel.lng))
                        .animate(CameraAnimation.Easing)

                naverMap.moveCamera(cameraUpdate)
            }

        })

        viewPagerAdapter.setItemClickListener {
            val intent = Intent()
                .apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "[지금 이 가격에 예약하세요!!] ${it.title} ${it.price} 사진보기 : ${it.imgUrl}")
                    type = "text/plain"
                }
            startActivity(Intent.createChooser(intent, null))
        }
    }

    private fun initUi() {
        mainBinding.mapView.getMapAsync(this)
        bottomSheetBinding.recyclerView.adapter = recyclerAdapter
        mainBinding.houseViewPager.adapter = viewPagerAdapter
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    return MainViewModel(RepositoryImpl(RemoteDataSourceImpl())) as T
                } else throw IllegalArgumentException()
            }
        })[MainViewModel::class.java]

        mainViewModel.mainViewStateLiveData.observe(this) { viewState ->
            when (viewState) {
                is MainViewState.GetHouseList -> {
                    updateMarker(viewState.dto.items)
                    viewPagerAdapter.submitList(viewState.dto.items)
                    recyclerAdapter.submitList(viewState.dto.items)
                    bottomSheetBinding.bottomSheetTitleTextView.text = "${viewState.dto.items.size}개의 숙소"
                }
                is MainViewState.Error -> {
                    Toast.makeText(this, viewState.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        // 줌 범위 설정
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        // 지도 위치 이동
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.497885, 127.027512))
        naverMap.moveCamera(cameraUpdate)

        // 현 위치 버튼 기능
        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = false
        mainBinding.currentLocationButton.map = naverMap

        // -> onRequestPermissionsResult // 위치 권한 요청
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource

        mainViewModel.requestHouseList()
    }

    private fun updateMarker(houses: List<HouseModel>) {
        houses.forEach { house ->
            Marker().apply {
                position = LatLng(house.lat, house.lng)
                onClickListener = this@MainActivity // 마커 클릭 시 뷰 페이져 연동 되도록 구현
                map = naverMap
                tag = house.id
                icon = MarkerIcons.BLACK
                iconTintColor = Color.RED
            }
        }
    }

    // 위치 퍼미션
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }

        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }

    }

    // 마커? 클릭 시 ViewPager 아이템 변경
    override fun onClick(overly: Overlay): Boolean {
        val selectedModel = viewPagerAdapter.currentList.firstOrNull {
            it.id == overly.tag
        }

        selectedModel?.let {
            val position = viewPagerAdapter.currentList.indexOf(it)
            mainBinding.houseViewPager.currentItem = position
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        mainBinding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mainBinding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mainBinding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mainBinding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mainBinding.mapView.onLowMemory()
    }

}