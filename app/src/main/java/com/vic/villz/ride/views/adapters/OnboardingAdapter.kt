package com.vic.villz.ride.views.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class OnboardingAdapter(fm: FragmentManager, val fragmentList:ArrayList<Fragment>) : FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int) = fragmentList.get(position)

    override fun getCount() = fragmentList.size

}