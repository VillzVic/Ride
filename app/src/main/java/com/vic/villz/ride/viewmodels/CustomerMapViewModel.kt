package com.vic.villz.ride.viewmodels

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CustomerMapViewModel(var app: Application) : AndroidViewModel(app){

    var customerId: String
    var requestRef: DatabaseReference
    var driverLocationRef: DatabaseReference
    var geofire : GeoFire
    var radius = 1.0
    var driverFound: Boolean = false
    var driverFoundId: String? = null
    var firebaseInstance:FirebaseDatabase

    var buttonTextLivedata:MutableLiveData<String> = MutableLiveData()
    var addDriverLocationMarkerLiveData:MutableLiveData<LatLng> = MutableLiveData()


    init {
        firebaseInstance = FirebaseDatabase.getInstance()
        customerId  = FirebaseAuth.getInstance().currentUser?.uid.toString()
        requestRef = FirebaseDatabase.getInstance().reference.child("CustomerRequests")
        driverLocationRef = FirebaseDatabase.getInstance().reference.child("DriversAvailable")
        geofire = GeoFire(requestRef)
    }


    fun setGeoFireLocation(location: Location){

        geofire.setLocation(customerId, GeoLocation(location.latitude, location.longitude)) { key, error ->
            Log.d(TAG, "data ${error}")
        }
    }

    fun removeGeoFireLocation(){
        geofire.removeLocation(customerId){ key, error ->
            Log.d(TAG, "data ${error}")
        }
    }

    //in the Ui you will be showing that loading screen until driver is found
    //get the closest driver based on radius, start from the center(customer location) and search by radius, increment the radius if driver us not found
    //create a livedata object that informs the view thatwe are looking for driver(particularly a linear progress bar)
    fun getClosestDriver(customerLocation : Location) {
        var driverGeoFire = GeoFire(driverLocationRef)
        var geoQuery = driverGeoFire.queryAtLocation(GeoLocation(customerLocation.latitude, customerLocation.longitude), radius)
        geoQuery.removeAllListeners()

        geoQuery.addGeoQueryEventListener(object :GeoQueryEventListener{
            override fun onGeoQueryReady() {
                //after searching and no driver was found
                if(!driverFound){
                    radius++
                    getClosestDriver(customerLocation)
                }

            }

            override fun onKeyEntered(key: String?, location: GeoLocation?) {
                //if a driver is found (when we enter one database key)
                if(!driverFound){
                    driverFound = true // update a livedata object that the driver has been found
                    driverFoundId = key

                    buttonTextLivedata.value = "Driver found"

                    //notify Driver Of CustomerRequest
                    val driverRef = driverFoundId?.let {
                        firebaseInstance.reference.child("Users")
                            .child("Drivers")
                            .child(it)
                            .child("AssignedCustomer")
                    }

                    var map = mutableMapOf<String, Any>().apply {
                        put("CustomerId", customerId)
                    }

                    driverRef?.updateChildren(map)
                    buttonTextLivedata.value = "Looking for driver's location"

                    //get driverLocation for the customer
                    getDriverLocation()
                }

            }

            override fun onKeyMoved(key: String?, location: GeoLocation?) {

            }

            override fun onKeyExited(key: String?) {

            }

            override fun onGeoQueryError(error: DatabaseError?) {

            }
        })

    }

    fun getDriverLocation() {
        //will be created on the driver side
        val driverLocationRef = driverFoundId?.let {
            firebaseInstance.reference.child("DriversWorking")
                .child(it)
                .child("l")
        }

        //reason for value event listener is because of the fact that we want real time data(location value will change as driver moves)
        driverLocationRef?.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
              if(dataSnapshot.exists()) { //if data exists, then location is found

                  var map = dataSnapshot.getValue() as List<Object>
                  var lat = 0.0
                  var lng = 0.0
                  buttonTextLivedata.value = "Driver location found"
                  lat = map?.get(0).toString().toDouble()
                  lng = map?.get(0).toString().toDouble()

                  var driverLocation = LatLng(lat, lng) //this is the driver's location
                  addDriverLocationMarkerLiveData.value = driverLocation //you can make this marker a moving car(custom image, since location will be changing as the customer moves)

              }
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
    }


    companion object{
        val TAG = "DRIVER VIEW MODEL"
    }




}