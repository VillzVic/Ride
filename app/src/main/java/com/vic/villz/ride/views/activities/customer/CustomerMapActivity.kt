package com.vic.villz.ride.views.activities.customer

import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.vic.villz.ride.R
import com.vic.villz.ride.viewmodels.CustomerMapViewModel
import kotlinx.android.synthetic.main.activity_customer_map.*

class CustomerMapActivity : AppCompatActivity(), OnMapReadyCallback,
    CustomerLocationInterface {

    private lateinit var mMap: GoogleMap
    val LOCATION_PERMISSION = 203
    private lateinit var locationManager: CustomerLocationManager
    private lateinit var customerMapViewModel: CustomerMapViewModel
    var userLocation: Location? = null
    private var driverMarker: Marker?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_map)

        customerMapViewModel = ViewModelProviders.of(this).get(CustomerMapViewModel::class.java)



        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.customer_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationManager = CustomerLocationManager(this, this)
        lifecycle.addObserver(locationManager)

        req_btn.setOnClickListener {
            makeRequest()
        }

        observeChanges()
    }

    private fun observeChanges() {
        //observe text on button
        customerMapViewModel.buttonTextLivedata.observe(this, Observer<String> {

            req_btn.text = it
        })


        //this is where you replace the marker with car that moves. since data is updating real-time
        customerMapViewModel.addDriverLocationMarkerLiveData.observe(this, Observer<LatLng> { latlng->
            //when the location change, remove the marker(car) and add a new one to the updated location
            driverMarker?.let {
                //If driver marker has been added before, then remove it
               driverMarker?.remove()
            }
            driverMarker = mMap.addMarker(MarkerOptions().position(latlng).title("Your driver"))
        })
    }

    override fun onResume() {
        super.onResume()
        if(::mMap.isInitialized){
            locationManager.initLocationTracking()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        styleMap()

        checkForPermission()
    }

    private fun styleMap() {
        try {
            var success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style_json))

            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }

    }

    private fun makeRequest(){
        req_btn.text = "Searching for driver"

        userLocation?.let { loc ->
            customerMapViewModel.setGeoFireLocation(loc)
            mMap.addMarker(MarkerOptions().position(LatLng(loc.latitude, loc.longitude)).title("Pickup here"))

            customerMapViewModel.getClosestDriver(loc)
        }
    }

    private fun checkForPermission() {

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            initMap()
        } else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == LOCATION_PERMISSION){
            if(permissions.size == 1 &&
                permissions[0] ==  android.Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ){
                initMap()
            }

        }
    }

    fun initMap(){
        mMap.isMyLocationEnabled = true

        locationManager.getLastLocation()

        locationManager.initLocationTracking()
    }

    private fun updateMapLocation(location: Location?) {

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0))
            .zoom(17f)
            .bearing(-45f)
            .tilt(50f)
            .build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null)
        location?.let {
            userLocation = it
        }

    }

    override fun OnLocationReceived(location: Location) {
        updateMapLocation(location)
    }

    companion object {
        private val TAG = CustomerMapActivity::class.java.simpleName
    }
}
