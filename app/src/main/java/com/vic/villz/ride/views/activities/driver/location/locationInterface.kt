package com.vic.villz.ride.views.activities.driver.location

import android.location.Location

interface LocationInterface{
    fun OnlocationReceived(location: Location)
    fun removeGeoFireLocation()
}