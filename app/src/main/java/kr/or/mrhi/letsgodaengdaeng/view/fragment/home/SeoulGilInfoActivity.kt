package kr.or.mrhi.letsgodaengdaeng.view.fragment.home

import android.Manifest.permission.*
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import kr.or.mrhi.letsgodaengdaeng.R
import kr.or.mrhi.letsgodaengdaeng.dataClass.SeoulGil
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivitySeoulGilInfoBinding

class SeoulGilInfoActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    lateinit var binding : ActivitySeoulGilInfoBinding
    var seoulGil: SeoulGil? = null
    var longitude: Double? = null
    var latitude: Double? = null
    lateinit var providerClient: FusedLocationProviderClient
    lateinit var apiClient: GoogleApiClient
    var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeoulGilInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        seoulGil = intent.getParcelableExtra("seoulGil")

        binding.btnBack.setOnClickListener { finish() }
        binding.tvCourseName.setText(seoulGil?.name)
        binding.tvName.text = seoulGil?.name
        binding.tvLocal.text = seoulGil?.local
        binding.tvDistance.text = seoulGil?.distance
        binding.tvTime.text = seoulGil?.time
        binding.tvDetailCourse.text = seoulGil?.detailCourse
        binding.tvContent.text = seoulGil?.content
        var level = seoulGil?.courseLevel
        if (level.equals("1")){
            level = "초급"
        }else if (level.equals("2")){
            level = "중급"
        }else{
            level = "고급"
        }
        binding.tvLevel.text = level
        longitude = seoulGil?.longitude!!.toDouble()
        latitude = seoulGil?.latitude!!.toDouble()

        //퍼미션을 여러개 요청
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            //요청했던 퍼미션이 모두 true 이면 실행함.
            if(it.all{ permission -> permission.value == true}){
                apiClient.connect()
            }else{
                Toast.makeText(this, "권한 승인 바랍니다.", Toast.LENGTH_SHORT).show()
            }
        }
        //프래그먼트에 지도동기화를 위해서 콜백함수는 현재 클래스에 있다.
        (supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment)!!.getMapAsync(this)
        //위치정보를 획득
        providerClient = LocationServices.getFusedLocationProviderClient(this)
        //위치정보를 획득하기 위한 접속
        apiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)  //API를 LoactionServices.API 를 사용하겠다.
            .addConnectionCallbacks(this)   //Location Provider가 이용가능한 상태에서 콜백함수는 현재 클래스에 있다.
            .addOnConnectionFailedListener(this)  //Location Provider가 이용 불가능한상태에서 콜백함수는 현재 클래스에 있다
            .build()

        //퍼미션이 승인이 되지 않으면 퍼미션을 요청하는 launch 기능
        if(ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, ACCESS_NETWORK_STATE) !== PackageManager.PERMISSION_GRANTED){
            requestPermissionLauncher.launch(
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    WRITE_EXTERNAL_STORAGE,
                    ACCESS_NETWORK_STATE
                )
            )
        }else{
            apiClient.connect() //apiClient에게 Location Provider 를 준비해볼것을 요청
        }
    }

    private fun moveMap(latitude: Double, longitude: Double){
        val latlng = LatLng(latitude, longitude)
        val position: CameraPosition = CameraPosition.Builder()
            .target(latlng)
            .zoom(16f)
            .build()
        googleMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))
        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        markerOptions.position(latlng)
        markerOptions.title("${seoulGil?.name}")

        googleMap?.addMarker(markerOptions)
    }

    override fun onConnected(p0: Bundle?) {
        if(ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED){
            providerClient.lastLocation.addOnSuccessListener(
                this@SeoulGilInfoActivity,
                object: OnSuccessListener<Location> {
                    override fun onSuccess(p0: Location?) {
                        p0?.let{
                            moveMap(latitude!!,longitude!!)
                        }
                    }
                }
            )
            apiClient.disconnect()
        }
    }
    //사용하고 있던 어떤 Location Provider가 더 이상 이용불가능한 상태가 되었을때
    override fun onConnectionSuspended(p0: Int) {  }
    //위치 정보 제공자(Location Provider)를 얻지 못했을때
    override fun onConnectionFailed(p0: ConnectionResult) { }
    //지도 객체가 이용가능한 상태가 되었을때
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
    }
}
