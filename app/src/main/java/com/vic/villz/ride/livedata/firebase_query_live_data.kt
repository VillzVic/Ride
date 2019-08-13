package com.vic.villz.ride.livedata

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.vic.villz.ride.livedata.FirebaseQueryLiveData.MyValueEventListener


class FirebaseQueryLiveData(var query: Query): LiveData<DataSnapshot>() {

    private val listener = MyValueEventListener()

    companion object{
        val LOG_TAG = "FirebaseQueryLiveData";
    }


    override fun onActive() {
        Log.d(LOG_TAG, "onActive");
        query.addValueEventListener(listener)
    }

    override fun onInactive() {
        Log.d(LOG_TAG, "onInactive");
        query.removeEventListener(listener)
    }

     inner class MyValueEventListener : ValueEventListener{

         override fun onDataChange(data: DataSnapshot) {
             value = data
         }

         override fun onCancelled(databaseError: DatabaseError) {
             Log.e(LOG_TAG, "Can't listen to query " + query, databaseError.toException())
         }

     }
}