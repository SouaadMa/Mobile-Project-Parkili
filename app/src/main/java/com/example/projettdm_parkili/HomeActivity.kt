package com.example.projettdm_parkili

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projettdm_parkili.adapters.ParkingLotsList_Adapter
import com.example.projettdm_parkili.databinding.ActivityHomeListBinding
import com.example.projettdm_parkili.models.ParkingLot

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityHomeListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        val recyclerView = findViewById<RecyclerView>(R.id.rv_parkings)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = ParkingLotsList_Adapter(this,loadData())

        binding.textViewMapView.setOnClickListener{
            if(binding.mapViewBackground.visibility == View.INVISIBLE) flipViews()
        }

        binding.textViewListView.setOnClickListener{
            if(binding.ListViewBackground.visibility == View.INVISIBLE) flipViews()
        }

    }

    private fun loadData(): List<ParkingLot> {
        val data = mutableListOf<ParkingLot>()
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        return data
    }

    private fun flipViews() {
        val backg_listview = binding.ListViewBackground
        val backg_mapview = binding.mapViewBackground
        if (backg_listview.visibility==View.VISIBLE) {
            backg_listview.visibility = View.INVISIBLE
            backg_mapview.visibility = View.VISIBLE
        }
        else {
            backg_mapview.visibility = View.INVISIBLE
            backg_listview.visibility = View.VISIBLE
        }
    }

}