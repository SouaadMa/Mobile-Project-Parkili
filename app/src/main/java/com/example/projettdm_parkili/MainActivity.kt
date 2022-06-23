package com.example.projettdm_parkili

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import com.example.projettdm_parkili.databinding.ActivityMainBinding
import com.example.projettdm_parkili.models.User
import com.example.projettdm_parkili.retrofit.EndPoint
import com.example.projettdm_parkili.utils.isLogin
import com.example.projettdm_parkili.utils.saveUserID
import com.example.projettdm_parkili.utils.saveUserName
import com.example.projettdm_parkili.validator.EmailValidator
import com.example.projettdm_parkili.validator.EmptyValidator
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(isLogin(this)) {
            openHomeActivity()
        }

        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        var button_signup = binding.buttonSignup
        button_signup.setOnClickListener {
            openSignUpActivity()
        }

        var button_signin = binding.buttonSignin
        button_signin.setOnClickListener {
            val email = binding.editTextTextPersonName.text.toString()
            val emailEmptyValidation = EmptyValidator(email).validate()
            val validEmailValidation = EmailValidator(email).validate()
            val pwd = binding.editTextTextPassword.text.toString()
            val pwdEmptyValidator = EmptyValidator(pwd).validate()

            var errormsg =
                if (!emailEmptyValidation.isSuccess)
                    emailEmptyValidation.message
                else if (!validEmailValidation.isSuccess)
                    validEmailValidation.message
                else if (!pwdEmptyValidator.isSuccess)
                    pwdEmptyValidator.message
                else null

            if(errormsg != null) Toast.makeText(this, errormsg, Toast.LENGTH_SHORT).show()
            else {
                callSignIn(this)
            }
        }

    }

    fun openSignUpActivity(){

        var intent = Intent(this, SignUpActivity::class.java )
        startActivity(intent)

    }

    fun callSignIn(ctx : Context) {
        CoroutineScope(Dispatchers.IO).launch  {
            val resp = signIn()
            withContext(Dispatchers.Main) {
                if (resp.isSuccessful && resp.body() != null) {
                    saveUserName(ctx, resp.body()?.fullname!!)
                    saveUserID(ctx, resp.body()?.userId!!)
                    openHomeActivity()
                } else {
                    Log.d("io", "resp is not successful")
                    val errormsg = "Could not login"
                    showErrorMsg(errormsg!!)
                }
            }
        }
    }

    suspend fun signIn() : Response<User> {

        return EndPoint.createInstance().login(
            User(
                email = binding.editTextTextPersonName.text.toString(),
                pwd = binding.editTextTextPassword.text.toString()
            )
        )
    }

    fun showErrorMsg(msg : String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    fun openHomeActivity(){

        var intent = Intent(this, NavHomeActivity::class.java )
        startActivity(intent)

    }
}