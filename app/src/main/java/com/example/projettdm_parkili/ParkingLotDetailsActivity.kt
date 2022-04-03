package com.example.projettdm_parkili

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.projettdm_parkili.databinding.ActivityParkinglotdetailsBinding
import com.example.projettdm_parkili.models.ParkingLot

class ParkingLotDetailsActivity : AppCompatActivity() {

    lateinit var parking: ParkingLot

    private lateinit var binding : ActivityParkinglotdetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityParkinglotdetailsBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        parking = intent.getSerializableExtra("parking") as ParkingLot

        fillTextViews()

        binding.buttonShowmap.setOnClickListener{
            goToMap()
        }


    }

    fun fillTextViews() {
        binding.textViewParkinglotname.text = parking.title
        binding.textViewParkinglotlocation.text = parking.location
        binding.textViewState.text = parking.state

        binding.textViewDistance.text = parking.distance.toString()
        binding.textViewDuration.text = parking.duration.toString()

        if (parking.state.equals("Closed", true) ){
            binding.textViewState.setTextColor(ContextCompat.getColor(this, R.color.badnews_red))
        }else{
            binding.textViewState.setTextColor(ContextCompat.getColor(this, R.color.goodnews_green))
        }
    }

    fun goToMap() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?daddr=${parking.location}")
        )
        startActivity(intent)
    }

}