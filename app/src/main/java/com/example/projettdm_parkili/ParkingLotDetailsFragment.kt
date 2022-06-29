package com.example.projettdm_parkili

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.work.*
import com.bumptech.glide.Glide
import com.example.projettdm_parkili.databinding.FragmentParkingLotDetailsBinding
import com.example.projettdm_parkili.models.ParkingLot
import com.example.projettdm_parkili.models.Review
import com.example.projettdm_parkili.services.ReviewSyncService
import com.example.projettdm_parkili.viewModels.ParkingViewModel


class ParkingLotDetailsFragment : Fragment() {

    private lateinit var binding: FragmentParkingLotDetailsBinding

    var data : ParkingLot? = null

    var position  = 0

    private var db : AppDatabase? = null

    private var starsArray : List<ImageView>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentParkingLotDetailsBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        position = arguments?.getInt("position")!!
        data = ViewModelProvider(requireActivity())[ParkingViewModel::class.java].data.value?.get(position!!)
        db = AppDatabase.buildDatabase(requireActivity())

        fillTextViews()
        binding.buttonShowmap.setOnClickListener{
            goToMap()
        }

        binding.buttonBook.setOnClickListener{
            goToBookingPage()
        }

        starsArray = mutableListOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)
        binding.star1.setOnClickListener{starClickListener(1)}
        binding.star2.setOnClickListener{starClickListener(2)}
        binding.star3.setOnClickListener{starClickListener(3)}
        binding.star4.setOnClickListener{starClickListener(4)}
        binding.star5.setOnClickListener{starClickListener(5)}

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
        val workManager = WorkManager.getInstance(requireActivity())
        workManager.enqueueUniqueWork("sync", ExistingWorkPolicy.REPLACE, req)
        Log.d("sync", "sent review $i")

    }

    fun fillTextViews() {
        binding.textViewParkinglotname.text = data?.name
        binding.textViewParkinglotlocation.text = data?.commune

        binding.textViewUnitPrice.text = data?.priceperhour.toString()

        binding.textViewOccupation.text = (data?.nb_occupiedSpots!!/(data?.nb_totalSpots!!)).toString() + "%"

        Glide.with(requireActivity()).load(url + data?.image).into(binding.ivParkingimage)

        //binding.textViewState.text =
        //rating
/*
        binding.textViewDistance.text = parking.distance.toString()
        binding.textViewDuration.text = parking.duration.toString()

        if (parking.state.equals("Closed", true) ){
            binding.textViewState.setTextColor(ContextCompat.getColor(this, R.color.badnews_red))
        }else{
            binding.textViewState.setTextColor(ContextCompat.getColor(this, R.color.goodnews_green))
        }*/
    }

    fun goToMap() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?daddr=${data?.commune}")
        )
        startActivity(intent)
    }

    fun goToBookingPage() {
        var bundle = bundleOf("position" to position)
        requireActivity().findNavController(R.id.navHost).navigate(R.id.action_fragment_details_to_fragment_booking, bundle)
    }

}