package com.example.projettdm_parkili

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.projettdm_parkili.databinding.ActivityMainBinding
import com.example.projettdm_parkili.models.User
import com.example.projettdm_parkili.retrofit.EndPoint
import com.example.projettdm_parkili.validator.EmailValidator
import com.example.projettdm_parkili.validator.EmptyValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                GlobalScope.launch {
                    val resp = signIn()
                    withContext(Dispatchers.Main) {
                        if (resp.isSuccessful) {
                            openHomeActivity()
                        } else {
                            Log.d("io", "resp is not successful")
                            errormsg = "Could not login"
                            showErrorMsg(errormsg!!)
                        }
                    }
                }
            }
        }

    }

    fun openSignUpActivity(){

        var intent = Intent(this, SignUpActivity::class.java )
        startActivity(intent)

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

        var intent = Intent(this, HomeActivity::class.java )
        startActivity(intent)

    }
}