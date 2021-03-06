package fastcampus.aop.part3.chapter07.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint
import fastcampus.aop.part3.chapter07.R
import fastcampus.aop.part3.chapter07.api.HouseModel
import fastcampus.aop.part3.chapter07.base.BaseActivity
import fastcampus.aop.part3.chapter07.data.repo.HouseRepositoryImpl
import fastcampus.aop.part3.chapter07.data.source.HouseRemoteDataSourceImpl
import fastcampus.aop.part3.chapter07.databinding.ActivityMainBinding
import fastcampus.aop.part3.chapter07.ui.adapter.HouseListAdapter
import fastcampus.aop.part3.chapter07.ui.adapter.HouseViewPagerAdapter

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), OnMapReadyCallback{

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val mainViewModel by viewModels<MainViewModel>()
    private val recyclerAdapter by lazy { HouseListAdapter() }
    private val viewPagerAdapter by lazy { HouseViewPagerAdapter() }

    private val overlayListener = Overlay.OnClickListener { overly->
        val selectedModel = viewPagerAdapter.currentList.firstOrNull {
            it.id == overly.tag
        }

        selectedModel?.let {
            val position = viewPagerAdapter.currentList.indexOf(it)
            binding.houseViewPager.currentItem = position
        }
        true
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)

        initUi()
        initViewModel()
    }

    private fun initUi() {

        with(binding) {
            bottomSheet.recyclerView.adapter = recyclerAdapter
            houseViewPager.adapter = viewPagerAdapter
            mapView.getMapAsync(this@MainActivity)

            houseViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val selectedHouseModel = viewPagerAdapter.currentList[position]
                    val cameraUpdate =
                        CameraUpdate.scrollTo(
                            LatLng(
                                selectedHouseModel.lat,
                                selectedHouseModel.lng
                            )
                        )
                            .animate(CameraAnimation.Easing)
                    naverMap.moveCamera(cameraUpdate)
                }
            })
        }


        recyclerAdapter.setItemClickListener {

        }

        viewPagerAdapter.setItemClickListener {
            val intent = Intent()
                .apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "[?????? ??? ????????? ???????????????!!] ${it.title} ${it.price} ???????????? : ${it.imgUrl}"
                    )
                    type = "text/plain"
                }
            startActivity(Intent.createChooser(intent, null))
        }
    }

    private fun initViewModel() {
        mainViewModel.mainViewStateLiveData.observe(this) { viewState ->
            when (viewState) {
                is MainViewState.GetHouseList -> {
                    viewPagerAdapter.submitList(viewState.dto.items)
                    recyclerAdapter.submitList(viewState.dto.items)
                    binding.bottomSheet.bottomSheetTitleTextView.text =
                        "${viewState.dto.items.size}?????? ??????"
                }
                is MainViewState.Error -> {
                    Toast.makeText(this, viewState.message, Toast.LENGTH_LONG).show()
                }

                is MainViewState.GetMarkerList -> {
                    viewState.list.forEach {
                        it.map = naverMap
                        it.onClickListener = overlayListener
                    }
                }
            }
        }
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        // ??? ?????? ??????
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        // ?????? ?????? ??????
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.497885, 127.027512))
        naverMap.moveCamera(cameraUpdate)

        // ??? ?????? ?????? ??????
        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = false
        binding.currentLocationButton.map = naverMap

        // -> onRequestPermissionsResult // ?????? ?????? ??????
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource

        mainViewModel.requestHouseList()
    }


    // ?????? ?????????
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

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

}