package com.vic.villz.ride.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    val mSuccessLiveData : MutableLiveData<Boolean> =  MutableLiveData()
    val mErrorLiveData : MutableLiveData<String> =  MutableLiveData()

    fun login(email:String, password:String){

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                mSuccessLiveData.value = true
            } else {
                it.addOnFailureListener {
                    Log.d(TAG, "login failure ${it.localizedMessage}")
                    mErrorLiveData.value = it.localizedMessage
                }
            }
        }


    }

    companion object{
        private var TAG = LoginViewModel::class.java.toString()
    }
}