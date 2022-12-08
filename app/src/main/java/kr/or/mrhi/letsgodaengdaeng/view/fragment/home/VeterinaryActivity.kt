package kr.or.mrhi.letsgodaengdaeng.view.fragment.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.Veterinary
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityVeterinaryBinding
import kr.or.mrhi.letsgodaengdaeng.view.activity.LoginActivity
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint

class VeterinaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityVeterinaryBinding
    var veterinary: Veterinary? = null
    val ACCESS_FINE_LOCATION = 1000
    var longitude: Double? = null
    var latitude: Double? = null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVeterinaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        veterinary = intent.getParcelableExtra("veterinary")

        setSupportActionBar(binding.toolVeterinary)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = veterinary?.name

        binding.tvHosName.text = veterinary?.name
        binding.tvAddress.text = veterinary?.address
        binding.tvPhone.text = veterinary?.phone
        binding.tvPhone.setLinkTextColor(R.color.rosy_brown)

        longitude = veterinary?.longitude!!.toDouble()
        latitude = veterinary?.latitude!!.toDouble()

        permissionCheck()
    }

    /** 위치 권한 확인 */
    fun permissionCheck() {
        val preference = getPreferences(MODE_PRIVATE)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 상태
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 권한 거절 (다시 한 번 물어보기)
                val builder = AlertDialog.Builder(this)
                builder.setMessage("현재 위치를 확인하시려면 위치 권한을 허용해주세요.")
                builder.setPositiveButton("그래요") { dialog, which ->
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                }
                builder.setNegativeButton("싫어요") { dialog, which ->
                    finish()
                }
                builder.show()
            }
        } else {
            // 권한이 있는 상태
            startMap()
        }
    }

    /** 권한 요청 후 행동 */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 요청 후 승인됨 (추적 시작)
                Toast.makeText(this, "위치 권한이 승인되었습니다", Toast.LENGTH_SHORT).show()
                startMap()
            } else {
                // 권한 요청 후 거절됨 (다시 요청 or 토스트)
                Toast.makeText(this, "위치 권한이 거절되었습니다", Toast.LENGTH_SHORT).show()
                permissionCheck()
            }
        }
    }

    /** 위치로 이동하여 커스텀 마커 찍기 */
    fun startMap() {
        binding.mapView.visibility = View.VISIBLE
        binding.mapView.zoomIn(true)
        binding.mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude!!,longitude!!), 1,true)

        val customMarker = MapPOIItem()
        customMarker.itemName = veterinary?.name
        customMarker.tag = 0
        customMarker.mapPoint = MapPoint.mapPointWithGeoCoord(latitude!!,longitude!!)
        customMarker.markerType = MapPOIItem.MarkerType.CustomImage
        customMarker.customImageResourceId = R.drawable.marker_hospital

        binding.mapView.addPOIItem(customMarker)
    }

    /** 툴바 백버튼 누르면 홈 프래그먼트로 돌아감 */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}