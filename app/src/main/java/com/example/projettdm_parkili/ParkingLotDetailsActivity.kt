package com.example.projettdm_parkili

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.*
import com.example.projettdm_parkili.databinding.ActivityParkinglotdetailsBinding
import com.example.projettdm_parkili.models.ParkingLot
import com.example.projettdm_parkili.models.Review
import com.example.projettdm_parkili.services.ReviewSyncService

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

        var star_array = mutableListOf<ImageView>(
            binding.star1,
            binding.star2,
            binding.star3,
            binding.star4,
            binding.star5,
        )

        var i = 1;
        while (i<=5) {
            star_array[i].setOnClickListener {
                val db = AppDatabase.buildDatabase(this)?.getReviewDao()?.addReview(
                    Review(i, "you are the best")
                )

                val constraints = Constraints.Builder(). setRequiredNetworkType(NetworkType.CONNECTED). // UNMETERED signifie réseau Wi-Fi
                build()
                val req = OneTimeWorkRequest.Builder(ReviewSyncService::class.java)
                    .setConstraints(constraints).build()
                val workManager = WorkManager.getInstance(this)
                workManager.enqueueUniqueWork("sync", ExistingWorkPolicy.REPLACE, req)
                Log.d("sync", "setOnClickListener $i")

            }
            i++
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