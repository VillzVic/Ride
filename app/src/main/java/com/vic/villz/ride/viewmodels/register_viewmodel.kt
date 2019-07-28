package com.vic.villz.ride.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel:ViewModel() {

     private val firebaseAuth = FirebaseAuth.getInstance()
     val firebaseDatabase = FirebaseDatabase.getInstance()
     val mSuccessLiveData : MutableLiveData<Boolean> =  MutableLiveData()
     val mErrorRegister : MutableLiveData<String> =  MutableLiveData()


    fun registeruser(email:String, username:String, password:String, isDriver:Boolean){

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful) {
                // put live data success
                val userId= firebaseAuth.currentUser?.uid


                userId?.let {
                    val usersNode = firebaseDatabase.reference.child("Users")
                    when(isDriver){
                        true -> {
                            storeUsersData("Drivers", userId, usersNode, username)
                        }
                        false -> {
                            storeUsersData("Customers", userId, usersNode, username)
                        }
                    }

                }
            }else{
                //put live date failure
                it.addOnFailureListener {
                    Log.d(TAG, "login failure ${it.localizedMessage}")
                    mErrorRegister.value = it.localizedMessage
                }
            }

        }
    }


    fun storeUsersData(userType:String, userId:String, usersNode:DatabaseReference, username:String){
        usersNode.child(userType).child(userId).child("username").setValue(username).addOnCompleteListener {
            if (it.isSuccessful) {
                mSuccessLiveData.value = true
            }else{
                it.addOnFailureListener {
                    Log.d(TAG, "sign up failure ${it.localizedMessage}")
                    mErrorRegister.value = it.localizedMessage
                    firebaseAuth.signOut()
                }
            }
        }
    }

    companion object{
        private var TAG = RegisterViewModel::class.java.toString()
    }

}