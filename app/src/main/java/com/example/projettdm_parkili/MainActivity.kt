package com.example.projettdm_parkili

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projettdm_parkili.databinding.ActivityMainBinding

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
            openHomeActivity()
        }

    }

    fun openSignUpActivity(){

        var intent = Intent(this, SignUpActivity::class.java )
        startActivity(intent)

    }

    fun openHomeActivity(){

        var intent = Intent(this, HomeActivity::class.java )
        startActivity(intent)

    }
}