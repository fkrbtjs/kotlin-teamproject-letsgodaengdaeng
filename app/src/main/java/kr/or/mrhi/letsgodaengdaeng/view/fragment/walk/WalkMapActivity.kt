package kr.or.mrhi.letsgodaengdaeng.view.fragment.walk

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.WalkMarker
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityWalkMapBinding
import kr.or.mrhi.letsgodaengdaeng.firebase.WalkDAO
import kr.or.mrhi.letsgodaengdaeng.view.activity.MainActivity
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class WalkMapActivity : AppCompatActivity() {
    lateinit var binding : ActivityWalkMapBinding
    val ACCESS_FINE_LOCATION = 1000     // Request Code
    var walkJob: Job? = null
    var walkJobFlag = false
    var point = 0
    val TAG = this.javaClass.simpleName

    var locationManager: LocationManager? = null
    var uLatitude: Double? = null
    var uLongitude: Double? = null
    var uNowPosition: MapPoint? = null
    var locationList = arrayListOf<WalkMarker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkMapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnStart.setOnClickListener {
            if (checkLocationService()) {
                // GPS가 켜져있을 경우
                permissionCheck()
            } else {
                // GPS가 꺼져있을 경우
                Toast.makeText(this, "GPS를 켜주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvStop.setOnClickListener {
            val intent = Intent(this, WalkFinishActivity::class.java)
            intent.putExtra("point", point)
            intent.putExtra("time", binding.tvTime.text)
            intent.putExtra("locationList",locationList)
            startActivity(intent)
            finish()
        }
    }

    // 위치 권한 확인
    private fun permissionCheck() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 상태
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 권한 거절 (다시 한 번 물어보기)
                val builder = AlertDialog.Builder(this)
                builder.setMessage("현재 위치를 확인하시려면 위치 권한을 허용해주세요.")
                builder.setPositiveButton("그래요") { dialog, which ->
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE), ACCESS_FINE_LOCATION)
                }
                builder.setNegativeButton("싫어요") { dialog, which ->
                    finish()
                }
                builder.show()
            }
        } else {
            // 권한이 있는 상태
            visible()
            startTracking()
        }
    }

    /** 권한 요청 후에 할 행동 */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 요청 후 승인이 되었다면 트래킹 모드를 활성화한다.
                Toast.makeText(this, "위치 권한이 승인되었습니다", Toast.LENGTH_SHORT).show()
                visible()
                startTracking()
            } else {
                // 권한 요청 후 거절되었다면 재요청한다.
                Toast.makeText(this, "위치 권한이 거절되었습니다", Toast.LENGTH_SHORT).show()
                permissionCheck()
            }
        }
    }

    /** GPS 가 켜져있는지 확인한다 */
    private fun checkLocationService(): Boolean {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /** 맵 트래킹 모드를 활성화 하여 현재 위치를 동기화 시킨다 */
    private fun startTracking() {
        binding.mapView.apply {
            setZoomLevelFloat(0.1f,false)
            currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        }
        binding.mapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.walkdog2, MapPOIItem.ImageOffset(16, 16))

        val walkDAO = WalkDAO()
        walkDAO.selectMarker()?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                locationList.clear()
                for (walkData in snapshot.children) {
                    val walk = walkData.getValue(WalkMarker::class.java)
                    val customMarker = MapPOIItem()
                    customMarker.itemName = walk?.userID
                    customMarker.mapPoint = MapPoint.mapPointWithGeoCoord(walk?.latitude!!.toDouble(),walk?.longitude!!.toDouble())
                    customMarker.markerType = MapPOIItem.MarkerType.CustomImage
                    customMarker.customImageResourceId = R.drawable.pawmarker

                    binding.mapView.addPOIItem(customMarker)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("WalkFragment", "selectUserPhone onCancelled $error")
            }
        })

        walkJobFlag = true
        start()
        binding.tvTime.start()
    }

    /** 액티비티 종료시 트래킹 모드가 활성화 되어 있다면 트래킹 모드를 정지시킨다 */
    override fun onDestroy() {
        binding.mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        walkJobFlag = false
        walkJob?.cancel()
        binding.tvTime.stop()
        super.onDestroy()
    }

    @SuppressLint("MissingPermission")
    fun start() {
        val backgroundScope = CoroutineScope(Dispatchers.Default + Job())
        walkJob = backgroundScope.launch {
            while (walkJobFlag == true) {
                try {
                    delay(6000)
                } catch (e: Exception) {
                    Log.d(TAG, "${e.stackTrace}")
                }
                runOnUiThread {
                    point++
                    binding.tvPoint.text = "$point"

                    val userNowLocation: Location? = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    //위도 , 경도
                    uLatitude = userNowLocation?.latitude
                    uLongitude = userNowLocation?.longitude
                    uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)

                    val customMarker = MapPOIItem()

                    customMarker.itemName = MainActivity.userInfo.nickname
                    customMarker.mapPoint = MapPoint.mapPointWithGeoCoord(uLatitude!!,uLongitude!!)
                    customMarker.markerType = MapPOIItem.MarkerType.CustomImage
                    customMarker.customImageResourceId = R.drawable.pawmarker
                    val walkMarker = WalkMarker(MainActivity.userInfo.nickname,"$uLatitude","$uLongitude")
                    locationList.add(walkMarker)
                    binding.mapView.addPOIItem(customMarker)

                    val walkDAO = WalkDAO()
                    walkDAO.insertMarker(walkMarker)
                }
            }
        }
    }

    fun visible() {
        binding.btnStart.visibility = View.GONE
        binding.cardView.visibility = View.VISIBLE
        binding.mapView.visibility = View.VISIBLE
    }
}