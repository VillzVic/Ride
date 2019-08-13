package com.vic.villz.ride.views.activities.driver.location

import android.content.Context
import android.location.Location
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.*


class LocationManager(var context: Context, var locationInterface: LocationInterface): LifecycleObserver{
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun init(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context.applicationContext)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun clean(){
        if(::locationCallback.isInitialized) {
            mFusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun removeObserver(){
        locationInterface.removeGeoFireLocation()
        (context as LifecycleOwner).lifecycle.removeObserver(this)
    }


    fun getLastLocation(){
        mFusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->

                location?.let { locationInterface.OnlocationReceived(it) }
            }

    }

    fun initLocationTracking() {

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                locationResult ?: return
                for (location in locationResult.locations){
                    locationInterface.OnlocationReceived(location)
                }
            }
        }

        mFusedLocationClient.requestLocationUpdates(LocationRequest() ,locationCallback,null)

    }

    companion object {
        private val TAG = LocationManager::class.java.simpleName
    }
}