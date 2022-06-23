package com.example.projettdm_parkili.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.projettdm_parkili.R
import com.example.projettdm_parkili.models.OpenSchedule
import com.example.projettdm_parkili.models.ParkingLot
import java.time.LocalDateTime
import java.time.chrono.ChronoLocalDateTime
import java.time.temporal.TemporalAccessor

class ParkingLotsList_Adapter (
        val context : Context,
        var data : List<ParkingLot>,
        var schedules : List<OpenSchedule>?,
        val onListItemClickedListener : (position:Int) -> Unit
    ) : RecyclerView.Adapter<ParkingLotsList_Adapter.ParkingHolder>() {


        class ParkingHolder(view: View, private val onListItemClickedListener: (position: Int) -> Unit) : RecyclerView.ViewHolder(view), View.OnClickListener {

            val title = view.findViewById(R.id.tv_parkingtitle) as TextView
            val state = view.findViewById(R.id.tv_parkingstate) as TextView
            val promotion = view.findViewById(R.id.tv_occupation) as TextView
            val location = view.findViewById(R.id.tv_location) as TextView
            val distance = view.findViewById(R.id.tv_distance) as TextView
            val duration = view.findViewById(R.id.tv_duration) as TextView

            val image = view.findViewById(R.id.iv_parkingimage) as ImageView

            init {
                view.setOnClickListener(this)
            }

            override fun onClick(p0: View?) {
                val position = adapterPosition
                onListItemClickedListener(position)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingHolder {
            return ParkingHolder(LayoutInflater.from(context).inflate(R.layout.parkinglot_item, parent, false), onListItemClickedListener)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("ResourceAsColor")
        override fun onBindViewHolder(holder: ParkingHolder, position: Int) {
            holder.apply {
                //title.text = data[position].name
                val pref = context.getSharedPreferences("Parkili_sharedpref",Context.MODE_PRIVATE)
                title.text = pref.getString("user_email", "none")

                var openornot = /*getOpenState(data[position])*/ "Open"
                state.text = openornot
                if(openornot == "Closed") {
                    state.setTextColor(Color.RED)
                }

                promotion.text = data[position].nb_occupiedSpots.toString()
                location.text = data[position].commune
                /*distance.text = data[position].distance.toString()
                duration.text = data[position].duration.toString()*/

                image.setImageResource(data[position].image)
            }
        }

        override fun getItemCount() = data.size

        @RequiresApi(Build.VERSION_CODES.O)
        fun getOpenState(parking : ParkingLot) : String {

            val now = LocalDateTime.now()
            val today = now.dayOfWeek.toString()
            Log.d("sched", today)

            val thisparkingScheduleOfToday = schedules?.filter { schedule ->
                schedule.parking_id.equals(parking.parking_id)
                        && schedule.dayOfWeek.equals(today)
            }

            Log.d("sched", "id :" + thisparkingScheduleOfToday!![0].parking_id.toString())


            return "Open"
        }

}