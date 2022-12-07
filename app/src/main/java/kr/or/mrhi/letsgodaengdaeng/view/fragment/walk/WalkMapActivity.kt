package kr.or.mrhi.letsgodaengdaeng.view.fragment.walk

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
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
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityWalkMapBinding

class WalkMapActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    lateinit var providerClient: FusedLocationProviderClient
    lateinit var apiClient: GoogleApiClient
    var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWalkMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            if(it.all{ permission -> permission.value == true}){
                apiClient.connect()
            }else{
                Toast.makeText(this, "권한 승인 바랍니다.", Toast.LENGTH_SHORT).show()
            }
        }
        (this@WalkMapActivity.supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment)!!.getMapAsync(this)
        providerClient = LocationServices.getFusedLocationProviderClient(this)
        apiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE
            ) !== PackageManager.PERMISSION_GRANTED){
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE
                )
            )
        }else{
            apiClient.connect()
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
        markerOptions.title("dd")

        googleMap?.addMarker(markerOptions)
    }

    override fun onConnected(p0: Bundle?) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED){
            providerClient.lastLocation.addOnSuccessListener(
                this@WalkMapActivity,
                object: OnSuccessListener<Location> {
                    override fun onSuccess(p0: Location?) {
                        p0?.let{
                            val latitude = p0.latitude
                            val longitude = p0.longitude
                            moveMap(latitude,longitude)
                        }
                    }
                }
            )
            apiClient.disconnect()
        }
    }
    override fun onConnectionSuspended(p0: Int) {  }
    override fun onConnectionFailed(p0: ConnectionResult) { }
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}