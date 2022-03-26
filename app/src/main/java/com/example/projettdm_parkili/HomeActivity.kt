package com.example.projettdm_parkili

import android.os.Bundle
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


    }

    private fun loadData(): List<ParkingLot> {
        val data = mutableListOf<ParkingLot>()
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1))
        return data
    }

}