package com.example.projettdm_parkili

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projettdm_parkili.adapters.ParkingLotsList_Adapter
import com.example.projettdm_parkili.databinding.ActivityHomeListBinding
import com.example.projettdm_parkili.models.OpenSchedule
import com.example.projettdm_parkili.models.ParkingLot
import com.example.projettdm_parkili.retrofit.EndPoint
import kotlinx.coroutines.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeListBinding

    var data : List<ParkingLot>? = null
    var schedules : List<OpenSchedule>? = null

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

        //loadData(recyclerView, this)

        binding.textViewMapView.setOnClickListener{
            if(binding.mapViewBackground.visibility == View.INVISIBLE) flipViews()
        }

        binding.textViewListView.setOnClickListener{
            if(binding.ListViewBackground.visibility == View.INVISIBLE) flipViews()
        }

    }

    private fun loadData(recyclerView : RecyclerView, ctx : Context) {

        val eP = EndPoint.createInstance()
/*
        CoroutineScope(Dispatchers.IO).launch {

            val resp2 = eP.getSchedules()

            withContext(Dispatchers.Main) {
                if (resp2.isSuccessful && resp2.body() != null) {
                    schedules = resp2.body()!!
                    Log.d("sched", "got schedules")
                }
            }

        }

 */

        CoroutineScope(Dispatchers.IO).launch {

            val resp = eP.getParkings()


            withContext(Dispatchers.Main) {
                if (resp.isSuccessful && resp.body() != null) {

                    data = resp.body()!!

                    recyclerView.adapter =
                        data?.let {
                            ParkingLotsList_Adapter(ctx,
                                onListItemClickedListener = { position -> onListItemClick(position) })
                        }
                }

            }

        }
/*
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        data.add(ParkingLot("Parking", "Closed", 40, "Algiers", 200.0, 20, R.drawable.parking1, 3.5, "08:00", "22:00", 400.0))
        return data

 */
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

    private fun onListItemClick(position: Int) {
        Toast.makeText(this, data?.get(position)?.name, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ParkingLotDetailsActivity::class.java)
        intent.putExtra("parking", data?.get(position))
        startActivity(intent)
    }

}