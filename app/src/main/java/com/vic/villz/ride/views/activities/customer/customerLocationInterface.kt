package com.vic.villz.ride.views.activities.customer

import android.location.Location


interface CustomerLocationInterface {
    fun OnLocationReceived(location: Location)
}