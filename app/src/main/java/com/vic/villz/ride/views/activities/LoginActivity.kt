package com.vic.villz.ride.views.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vic.villz.ride.R
import com.vic.villz.ride.persistence.UserDetailsSharedPreferences
import com.vic.villz.ride.services.UserType
import com.vic.villz.ride.services.showSnack
import com.vic.villz.ride.viewmodels.LoginViewModel
import com.vic.villz.ride.views.activities.driver.DriverMapActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog
    private lateinit var  loginViewModel: LoginViewModel
    private lateinit var userDetailsPrefs: UserDetailsSharedPreferences
    var  mapIntent:Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        userDetailsPrefs = UserDetailsSharedPreferences(this)
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        setUpLoginViewModel()

        progressDialog = ProgressDialog(this)


        setUpClickListeners()
    }

    private fun setUpLoginViewModel() {
        loginViewModel.mSuccessLiveData.observe(this, Observer {
            if(it){
               moveToLogin()
            }
        })

        loginViewModel.mErrorLiveData.observe(this, Observer{
            progressDialog.dismiss()
            showSnack(login_page, it.toString())
        })
    }

    private fun moveToLogin() {
        progressDialog.dismiss()

        if(driver_login_check.isChecked){
            mapIntent = Intent(this@LoginActivity, DriverMapActivity::class.java)

            userDetailsPrefs.setUserType(UserType.DRIVER.toString())
        }else{
            mapIntent = Intent(this@LoginActivity, CustomerMapActivity::class.java)
            userDetailsPrefs.setUserType(UserType.USER.toString())
        }


        mapIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(mapIntent)
        overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim)


    }

    private fun setUpClickListeners() {
        back_to_sign_up.setOnClickListener {
            onBackPressed()
        }

        login_button.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        if(login_email.text.isNullOrEmpty() or login_password.text.isNullOrEmpty()) {
            showSnack(sign_up_page, "All fields are compulsory")
        } else {
            progressDialog.setMessage("Please wait..")
            progressDialog.show()
            loginViewModel.login(login_email.text.toString(), login_password.text.toString())
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
    }
}
