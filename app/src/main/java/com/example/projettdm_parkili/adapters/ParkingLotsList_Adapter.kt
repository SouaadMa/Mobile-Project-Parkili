package com.example.projettdm_parkili.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projettdm_parkili.R
import com.example.projettdm_parkili.models.ParkingLot

class ParkingLotsList_Adapter (
        val context : Context,
        var data : List<ParkingLot>,
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

        @SuppressLint("ResourceAsColor")
        override fun onBindViewHolder(holder: ParkingHolder, position: Int) {
            holder.apply {
                title.text = data[position].title
                state.text = data[position].state
                if(data[position].state == "Closed") {
                    state.setTextColor(Color.RED)
                    Log.println(Log.INFO, null, "hi")
                }

                promotion.text = data[position].occupation.toString()
                location.text = data[position].location
                distance.text = data[position].distance.toString()
                duration.text = data[position].duration.toString()

                image.setImageResource(data[position].image)
            }
        }

        override fun getItemCount() = data.size

}