package com.vic.villz.ride.services.models.onboardingmodels

import com.vic.villz.ride.R

import kotlinx.android.synthetic.main.activity_splash_screen.view.*

class OnboardingItems{

    companion object {
        var onboardingItem1 = OnboardingItem(R.drawable.ic_track_driver)
        var onboardingItem2 = OnboardingItem(R.drawable.ic_city_building_1)
        var onboardingItem3 = OnboardingItem(R.drawable.ic_credit_card)


        var items: Array<OnboardingItem> = arrayOf(onboardingItem1, onboardingItem2, onboardingItem3)


        fun getOnboardingItems() = items
    }

}