package com.vic.villz.ride.views.activities.onboarding


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.vic.villz.ride.R
import kotlinx.android.synthetic.main.fragment_onboarding.*
import com.bumptech.glide.request.RequestOptions

import com.vic.villz.ride.R.*
import com.vic.villz.ride.services.models.onboardingmodels.OnboardingItem


private const val ARG_PARAM1 = "image"



class OnboardingFragment : Fragment() {

    private var onboardingItem: OnboardingItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            onboardingItem = it.getParcelable(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        return inflater.inflate(layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        onboardingItem?.let {
//            val requestOptions = RequestOptions()
//                .placeholder(drawable.ic_launcher_background)

            activity?.let {
                Glide.with(it).load(onboardingItem!!.image).into(item_image)
            }
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(item: OnboardingItem) =
            OnboardingFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, item)
                }
            }
    }
}
