package com.vic.villz.ride.views.activities.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.vic.villz.ride.R
import com.vic.villz.ride.persistence.UserDetailsSharedPreferences
import com.vic.villz.ride.services.models.onboardingmodels.OnboardingItems
import com.vic.villz.ride.services.models.onboardingmodels.OnboardingTexts
import com.vic.villz.ride.views.activities.SignUpActivity
import com.vic.villz.ride.views.adapters.OnboardingAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : AppCompatActivity() {
    val compositeDisposable = CompositeDisposable()
    var animation : Animation? = null
    private lateinit var userDetailsPrefs: UserDetailsSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        animation = AnimationUtils.loadAnimation(this, R.anim.onboarding_text_anim)
        userDetailsPrefs = UserDetailsSharedPreferences(this)

        setupViewPager()
        setUpfirstText()
        setupClickListeners()

    }

    private fun beginAnimation() {
        header.startAnimation(animation)
        description.startAnimation(animation)
    }

    private fun setUpfirstText() {
        val onboardingText = OnboardingTexts.getOnboardingTexts()[0]

        header.text = onboardingText.header
        description.text = onboardingText.description

        beginAnimation()
    }

    private fun setupViewPager() {
        val fragmentList = ArrayList<Fragment>()


        for (item in OnboardingItems.getOnboardingItems()){
            fragmentList.add(OnboardingFragment.newInstance(item))
        }


        viewPager.adapter = OnboardingAdapter(supportFragmentManager, fragmentList)
        tab_layout.setupWithViewPager(viewPager)


        viewPager.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                val onboardingText = OnboardingTexts.getOnboardingTexts()[position]

                header.text = onboardingText.header
                description.text = onboardingText.description
            }
        })

    }


    private fun setupClickListeners() {

        floatingActionButton2.setOnClickListener {
            if (viewPager.currentItem < viewPager.childCount){

                viewPager.setCurrentItem(viewPager.currentItem + 1, true)


            } else if (viewPager.currentItem == viewPager.childCount) {
                //navigate to sign up
                navigateToSignUpActivity()
            }
        }

        skiptext.setOnClickListener {
            navigateToSignUpActivity()
        }

    }

    private fun navigateToSignUpActivity() {


        val intent = Intent(this, SignUpActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        userDetailsPrefs.setIsFirstTime(false)
    }



    override fun onResume() {
        super.onResume()
        compositeDisposable.clear()
    }

}
