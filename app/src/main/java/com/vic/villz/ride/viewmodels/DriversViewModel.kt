package com.vic.villz.ride.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DriversViewModel: ViewModel() {

    private var driverId: String
    private var refAvailable: DatabaseReference
    private var refWorking: DatabaseReference
    private var geoFireAvailable : GeoFire
    private var geoFireWorking : GeoFire
    private var firebaseInstance: DatabaseReference
    private var assignedCustomerReference: DatabaseReference? = null
    private var assignedCustomerLocationRef: DatabaseReference? = null
    private var customerId: String? = null

    var addPickUpLocationMarkerLiveData: MutableLiveData<LatLng> = MutableLiveData()


    init {
        driverId  = FirebaseAuth.getInstance().currentUser?.uid.toString()
        refAvailable = FirebaseDatabase.getInstance().getReference("DriversAvailable")
        refWorking = FirebaseDatabase.getInstance().getReference("DriversWorking")

        geoFireAvailable = GeoFire(refAvailable)
        geoFireWorking = GeoFire(refWorking)
        firebaseInstance = FirebaseDatabase.getInstance().reference
    }

    //get assigned customer id
    fun getAssignedCustomer(){
        assignedCustomerReference = firebaseInstance.child("Users").child("Drivers").child(driverId).child("AssignedCustomer")

        assignedCustomerReference?.addValueEventListener(assignedCustomerValueEventListener) //cos data might change regularly

    }



    var assignedCustomerValueEventListener = object:ValueEventListener{
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if(dataSnapshot.exists()){
                val map = dataSnapshot.getValue() as Map<String, Any>
                customerId = map?.get("CustomerId").toString()

                //get that customer location from
                getAssignedCustomerPickUpLocation()
            }
        }
    }


    private fun getAssignedCustomerPickUpLocation() { //get that location from customer request
        assignedCustomerLocationRef = customerId?.let {
            firebaseInstance.child("CustomerRequests")
                .child(it)
                .child("l")
        }

        assignedCustomerLocationRef?.addValueEventListener(customerLocationValueEventListener)
    }


    var customerLocationValueEventListener =  object: ValueEventListener{
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if(dataSnapshot.exists()) {
                var map = dataSnapshot.getValue() as List<Object>
                var lat = 0.0
                var lng = 0.0

                lat = map?.get(0).toString().toDouble()
                lng = map?.get(0).toString().toDouble()

                var customerLocation = LatLng(lat, lng)
                addPickUpLocationMarkerLiveData.value = customerLocation
            }
        }

    }

    //always called every second so we can update driver available and driver working(can change if customer cancels location)
    fun setGeoFireLocation(location: Location){
        //we first check if the driver has any customer assigned to him, if not then we push location to driver available
        when(customerId){
            //if no customer is assigned to driver, he is available
            "" -> setDriverAvailable(location)

            else -> setDriverWorking(location)
        }
    }

    fun setDriverAvailable(location: Location){
        geoFireWorking.removeLocation(driverId){ key, error ->
            Log.d(TAG, "data ${error}")
        }

        geoFireAvailable.setLocation(driverId, GeoLocation(location.latitude, location.longitude)) { key, error ->
            Log.d(TAG, "data ${error}")
        }
    }

    //enables user to get driver location
    fun setDriverWorking(location: Location) {
        geoFireAvailable.removeLocation(driverId){ key, error ->
            Log.d(TAG, "data ${error}")
        }

        geoFireWorking.setLocation(driverId, GeoLocation(location.latitude, location.longitude)) { key, error ->
            Log.d(TAG, "data ${error}")
        }
    }


    fun removeGeoFireLocation(){
        geoFireAvailable.removeLocation(driverId){ key, error ->
            Log.d(TAG, "data ${error}")
        }
    }



    override fun onCleared() {
        super.onCleared()
        assignedCustomerReference?.removeEventListener(assignedCustomerValueEventListener)
        assignedCustomerLocationRef?.removeEventListener(customerLocationValueEventListener)
    }



    companion object{
        val TAG = "DRIVER VIEW MODEL"
    }
}