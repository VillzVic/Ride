package com.vic.villz.ride.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DriversViewModel: ViewModel() {

    var userId: String
    var databaseRef: DatabaseReference
    var geofire : GeoFire

    init {
        userId  = FirebaseAuth.getInstance().currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance().getReference("DriversAvailable")
        geofire = GeoFire(databaseRef)
    }



    fun setGeoFireLocation(location: Location){

        geofire.setLocation(userId, GeoLocation(location.latitude, location.longitude)) { key, error ->
            Log.d(TAG, "data ${error}")
        }
    }

    fun removeGeoFireLocation(){
        geofire.removeLocation(userId){key, error ->
            Log.d(TAG, "data ${error}")
        }
    }


    override fun onCleared() {
        super.onCleared()
    }


    companion object{
        val TAG = "DRIVER VIEW MODEL"
    }
}