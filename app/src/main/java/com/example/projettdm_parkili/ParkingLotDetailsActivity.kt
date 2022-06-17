package com.example.projettdm_parkili

import android.content.Intent
import android.media.Image
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

    private var db : AppDatabase? = null

    private var starsArray : List<ImageView>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityParkinglotdetailsBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        parking = intent.getSerializableExtra("parking") as ParkingLot

        db = AppDatabase.buildDatabase(this)

        fillTextViews()

        binding.buttonShowmap.setOnClickListener{
            goToMap()
        }

        Log.d("details", "before stars")

        starsArray = mutableListOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)

        binding.star1.setOnClickListener{starClickListener(1)}
        binding.star2.setOnClickListener{starClickListener(2)}
        binding.star3.setOnClickListener{starClickListener(3)}
        binding.star4.setOnClickListener{starClickListener(4)}
        binding.star5.setOnClickListener{starClickListener(5)}

        Log.d("details", "after stars")

    }

    fun starClickListener(i : Int) {

        var j = 0
        while(j <= i-1) {
            starsArray?.get(j)?.setImageResource(R.drawable.ic_half_star)
            j++
        }
        while(j<5) {
            starsArray?.get(j)?.setImageResource(R.drawable.ic_empty_star)
            j++
        }

        db?.getReviewDao()?.addReview(
            Review(note = i, comment = "great parking")
        )
        val constraints = Constraints.Builder(). setRequiredNetworkType(NetworkType.CONNECTED). // UNMETERED signifie réseau Wi-Fi
        build()
        val req = OneTimeWorkRequest.Builder(ReviewSyncService::class.java)
            .setConstraints(constraints).build()
        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniqueWork("sync", ExistingWorkPolicy.REPLACE, req)
        Log.d("sync", "sent review $i")

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