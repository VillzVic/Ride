package com.vic.villz.ride.views.activities

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vic.villz.ride.R
import java.util.jar.Manifest

class DriverMapActivity : AppCompatActivity(), OnMapReadyCallback , GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener{


    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    val LOCATION_PERMISSION = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_map)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        checkLocation()
    }

    override fun onResume() {
        super.onResume()
        if(::mMap.isInitialized){
            initLocationTracking()
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
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
            }
        }
    }

    private fun initMap() {
        //set location after permission granted
        mMap.isMyLocationEnabled = true
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->

                    updateMapLocation(location)

            }

        initLocationTracking()

    }

    //continuosly update user's location
    private fun initLocationTracking(){

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                locationResult ?: return
                for (location in locationResult.locations){
                    updateMapLocation(location)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(LocationRequest() ,locationCallback,null)

    }

    private fun updateMapLocation(location: Location?) {

        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)))

        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f))


    }




    override fun onMyLocationButtonClick(): Boolean {

        return false
    }

    override fun onMyLocationClick(p0: Location) {

    }
}
