package com.example.projettdm_parkili


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.work.ListenableWorker
import com.example.projettdm_parkili.databinding.ActivitySignupBinding
import com.example.projettdm_parkili.models.Review
import com.example.projettdm_parkili.models.User
import com.example.projettdm_parkili.retrofit.EndPoint
import com.example.projettdm_parkili.validator.EmailValidator
import com.example.projettdm_parkili.validator.EmptyValidator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private var db : AppDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivitySignupBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        var link_signin = binding.textViewAlreadyhaveanaccount
        link_signin.setOnClickListener {
            openSignInActivity()
        }

        var signup_button = binding.buttonSignup
        signup_button.setOnClickListener{
            val username = binding.editTextTextPersonName.text.toString()
            val usernameEmptyValidation = EmptyValidator(username).validate()
            val email = binding.editTextEmail.text.toString()
            val validEmailValidation = EmailValidator(email).validate()

            var errormsg =
                if (!usernameEmptyValidation.isSuccess)
                    usernameEmptyValidation.message
                else if (!validEmailValidation.isSuccess)
                    validEmailValidation.message
                else null

            if(errormsg != null) Toast.makeText(this, errormsg, Toast.LENGTH_SHORT).show()
            else
            GlobalScope.launch {
                signUp()
            }
        }

    }

    suspend fun signUp() {



        //check that all is good before inserting
        if (true) {

            val resp = EndPoint.createInstance().addNewUser(
                User(fullname = binding.editTextTextPersonName.text.toString(),
                    email = binding.editTextEmail.text.toString(),
                    phonenum = binding.editTextPhoneNumber.text.toString(),
                    pwd = binding.editTextTextPassword.text.toString())
            )

        }

    }

    fun openSignInActivity() {

        var intent = Intent(this, MainActivity::class.java )
        startActivity(intent)

    }

}