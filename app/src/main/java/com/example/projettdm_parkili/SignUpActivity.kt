package com.example.projettdm_parkili


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projettdm_parkili.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

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

    }

    fun openSignInActivity() {

        var intent = Intent(this, MainActivity::class.java )
        startActivity(intent)

    }

}