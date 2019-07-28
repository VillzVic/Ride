package com.vic.villz.ride.services

import android.app.ProgressDialog.show
import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.time.Duration

fun Context.showSnack(view: View, message:String){
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}