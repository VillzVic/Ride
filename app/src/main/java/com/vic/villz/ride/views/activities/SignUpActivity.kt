package com.vic.villz.ride.views.activities

import android.app.ActivityOptions
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.vic.villz.ride.R
import com.vic.villz.ride.persistence.UserDetailsSharedPreferences
import com.vic.villz.ride.services.UserType
import com.vic.villz.ride.services.showSnack
import com.vic.villz.ride.viewmodels.RegisterViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.card_container

class SignUpActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var  registerViewModel: RegisterViewModel
    private lateinit var userDetailsPrefs: UserDetailsSharedPreferences
    var  mapIntent:Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        userDetailsPrefs = UserDetailsSharedPreferences(this)

        progressDialog = ProgressDialog(this)
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        performAnimation()
        setUpClickListeners()

        setUpRegisterViewModel()



    }

    private fun setUpRegisterViewModel() {


        registerViewModel.mSuccessLiveData.observe(this, Observer<Boolean> {
            if(it){
                moveToMapActivity()
            }

        })

        registerViewModel.mErrorRegister.observe(this, Observer<String> {
            progressDialog.dismiss()
            showSnack(sign_up_page, it.toString())
        })
    }

    private fun moveToMapActivity(){
        progressDialog.dismiss()
        if(driver_check.isChecked){
            mapIntent = Intent(this@SignUpActivity, DriverMapActivity::class.java)
            userDetailsPrefs.setUserType(UserType.DRIVER.toString())

        }else{
            mapIntent = Intent(this@SignUpActivity, MapActivity::class.java)
            userDetailsPrefs.setUserType(UserType.USER.toString())
        }


        mapIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(mapIntent)
    }

    private fun setUpClickListeners() {
        login.setOnClickListener {
            moveToLogin()
        }


        signup_button.setOnClickListener {
            performSignUp()
        }
    }

    private fun performSignUp() {
        if(sign_up_email.text.isNullOrEmpty() or sign_up_username.text.isNullOrEmpty() or sign_up_password.text.isNullOrEmpty()){
            showSnack(sign_up_page, "All fields are compulsory")
        }else{
            progressDialog.setMessage("Please wait..")
            progressDialog.show()
            registerViewModel.registeruser(sign_up_email.text.toString(), sign_up_username.text.toString(), sign_up_password.text.toString(), driver_check.isChecked)
        }
    }

    private fun moveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)

        val options = ActivityOptions
            .makeSceneTransitionAnimation(this, car, "car_transition")

        startActivity(intent, options.toBundle())
    }

    private fun performAnimation() {
        val carAnim = AnimationUtils.loadAnimation(this, R.anim.sign_up_car_anim)
        val cardAnim = AnimationUtils.loadAnimation(this, R.anim.sign_up_content_anim)

        car.startAnimation(carAnim)
        card_container.startAnimation(cardAnim)
        login.startAnimation(cardAnim)
    }



}
