package com.vic.villz.ride.persistence

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.vic.villz.ride.services.UserType

class UserDetailsSharedPreferences(var context: Context){

    val IS_FIRST_TIME = "isfirsttime"
    val USER_TYPE = "user_type"
    val PREFS_NAME = "ride_prefs"

    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    var editor = sharedPreferences.edit()


    fun setIsFirstTime(bool: Boolean){
        editor.putBoolean(IS_FIRST_TIME, bool)
        editor.commit()
    }

    fun getIsFirstTime():Boolean{
        return sharedPreferences.getBoolean(IS_FIRST_TIME, true)
    }

    fun setUserType(type:String){
        editor.putString(USER_TYPE, type)
        editor.commit()
    }

    fun getUserType():String? {
        return sharedPreferences.getString(USER_TYPE, UserType.USER.toString())
    }


}

