package com.vic.villz.ride.views.activities.driver

import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.vic.villz.ride.R
import com.google.android.gms.maps.model.CameraPosition
import com.vic.villz.ride.viewmodels.DriversViewModel
import com.vic.villz.ride.views.activities.driver.location.LocationInterface
import com.vic.villz.ride.views.activities.driver.location.LocationManager


class DriverMapActivity : AppCompatActivity(), OnMapReadyCallback , LocationInterface {

    private lateinit var mMap: GoogleMap
    val LOCATION_PERMISSION = 101
    private lateinit var locationManager: LocationManager
    private lateinit var driversViewModel: DriversViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_map)

        driversViewModel = ViewModelProviders.of(this).get(DriversViewModel::class.java)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        locationManager = LocationManager(this, this)

        lifecycle.addObserver(locationManager)

        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        styleMap()

        checkLocation()
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

    override fun onResume() {
        super.onResume()
        if(::mMap.isInitialized){
            locationManager.initLocationTracking()
        }
    }


    private fun checkLocation() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            initMap()
        } else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION){
            if (permissions.size == 1 &&
                permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ){
                initMap()
            } else{
                //call check location
            }
        }
    }

    private fun initMap() {
        //set location after permission granted and other map data
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

        location?.let { driversViewModel.setGeoFireLocation(it) }

    }

    override fun OnlocationReceived(location: Location) {
        updateMapLocation(location)
    }

    override fun removeGeoFireLocation() {
        driversViewModel.removeGeoFireLocation()
    }

    companion object {
        private val TAG = DriverMapActivity::class.java.simpleName
    }
}
