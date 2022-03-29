package com.example.projettdm_parkili

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projettdm_parkili.databinding.ActivityParkinglotdetailsBinding

class ParkingLotDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityParkinglotdetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityParkinglotdetailsBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()



    }

}