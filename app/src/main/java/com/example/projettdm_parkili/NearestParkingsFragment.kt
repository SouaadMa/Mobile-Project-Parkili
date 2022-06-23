package com.example.projettdm_parkili

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projettdm_parkili.adapters.ParkingLotsList_Adapter

import com.example.projettdm_parkili.databinding.FragmentNearestParkingsBinding
import com.example.projettdm_parkili.models.OpenSchedule
import com.example.projettdm_parkili.models.ParkingLot
import com.example.projettdm_parkili.retrofit.EndPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NearestParkingsFragment : Fragment() {

    private lateinit var binding: FragmentNearestParkingsBinding

    var data : List<ParkingLot>? = null
    var schedules : List<OpenSchedule>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentNearestParkingsBinding.inflate(layoutInflater)
        val view = binding.root

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.rv_parkings)

        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager
        loadData(recyclerView, requireActivity())

        binding.textViewMapView.setOnClickListener {
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
                                it,
                                schedules,
                                onListItemClickedListener = { position -> onListItemClick(position) })
                        }
                }

            }

        }
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
        Toast.makeText(requireActivity(), data?.get(position)?.name, Toast.LENGTH_SHORT).show()
        val intent = Intent(requireActivity(), ParkingLotDetailsActivity::class.java)
        intent.putExtra("parking", data?.get(position))
        startActivity(intent)
    }
}