package com.vic.villz.ride.views.activities.customer

import android.content.Context
import android.location.Location
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.*
import com.vic.villz.ride.views.activities.driver.location.LocationManager

class CustomerLocationManager(var context: Context, var customerLocationInterface: CustomerLocationInterface) : LifecycleObserver{
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
        (context as LifecycleOwner).lifecycle.removeObserver(this)
    }


    fun getLastLocation(){
        mFusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->

                location?.let { customerLocationInterface.OnLocationReceived(it) }
            }

    }

    fun initLocationTracking() {

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                locationResult ?: return
                for (location in locationResult.locations){
                    customerLocationInterface.OnLocationReceived(location)
                }
            }
        }

        mFusedLocationClient.requestLocationUpdates(LocationRequest() ,locationCallback,null)

    }

    companion object {
        private val TAG = CustomerLocationManager::class.java.simpleName
    }
}