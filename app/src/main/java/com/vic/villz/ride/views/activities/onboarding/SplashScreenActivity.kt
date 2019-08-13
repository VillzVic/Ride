package com.vic.villz.ride.views.activities.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.google.firebase.auth.FirebaseAuth
import com.vic.villz.ride.R
import com.vic.villz.ride.persistence.UserDetailsSharedPreferences
import com.vic.villz.ride.services.UserType
import com.vic.villz.ride.views.activities.driver.DriverMapActivity
import com.vic.villz.ride.views.activities.CustomerMapActivity
import com.vic.villz.ride.views.activities.SignUpActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreenActivity : AppCompatActivity() {
    private lateinit var userDetailsPrefs: UserDetailsSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        userDetailsPrefs = UserDetailsSharedPreferences(this)

        performAnimation()
        initSplashScreen()
    }

    private fun initSplashScreen() {

        Handler().postDelayed({


            val user = FirebaseAuth.getInstance().currentUser

            if(user !=null){
                if(userDetailsPrefs.getUserType() == UserType.DRIVER.toString()){
                    navigate(DriverMapActivity::class.java)
                }else{
                    navigate(CustomerMapActivity::class.java)
                }

            } else{
                if(userDetailsPrefs.getIsFirstTime()){
                    navigate(OnboardingActivity::class.java)

                }else {
                    navigate(SignUpActivity::class.java)

                }
            }



        }, 4000)

    }

    private fun <T: Any> navigate(receiver: Class<T>) {
        val intent = Intent(this@SplashScreenActivity, receiver)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun performAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.splash_screen_header_fade)
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.splash_screen_bottom)

        ride_text.startAnimation(animation)
        taxi.startAnimation(animation)
        ride_description.startAnimation(animation2)


    }


    override fun onPause() {
        super.onPause()
        finish()
    }
}
