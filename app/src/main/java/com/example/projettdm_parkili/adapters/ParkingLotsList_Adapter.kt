package com.example.projettdm_parkili.adapters

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projettdm_parkili.MainActivity
import com.example.projettdm_parkili.R
import com.example.projettdm_parkili.models.OpenSchedule
import com.example.projettdm_parkili.models.ParkingLot
import com.example.projettdm_parkili.models.Reservation
import com.example.projettdm_parkili.url
import com.example.projettdm_parkili.utils.getUserName
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.location.LocationServices
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL
import java.time.LocalDateTime
import java.time.chrono.ChronoLocalDateTime
import java.time.temporal.TemporalAccessor

class ParkingLotsList_Adapter (
        val context : Context,
        val onListItemClickedListener : (position:Int) -> Unit
    ) : RecyclerView.Adapter<ParkingLotsList_Adapter.ParkingHolder>() {

    var data = mutableListOf<ParkingLot>()
    var schedules = mutableListOf<OpenSchedule>()
    public var userPos = mutableListOf(0.0, 0.0)

    class ParkingHolder(view: View, private val onListItemClickedListener: (position: Int) -> Unit) : RecyclerView.ViewHolder(view), View.OnClickListener {

            val title = view.findViewById(R.id.tv_parkingtitle) as TextView
            val state = view.findViewById(R.id.tv_parkingstate) as TextView
            val occupation = view.findViewById(R.id.tv_occupation) as TextView
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
                title.text = data[position].name

                var openornot = /*getOpenState(data[position])*/ "Open"
                state.text = openornot
                if(openornot == "Closed") {
                    state.setTextColor(Color.RED)
                }

                occupation.text = (data[position].nb_totalSpots / data[position].nb_occupiedSpots).toString() + "%"

                location.text = data[position].commune


                if(userPos[0] != 0.0 && userPos[1] != 0.0) {
                    var distTimePair = getDistanceAndTime(userPos[0], userPos[1], data[position].positionlat, data[position].positionlng)
                    Log.d("distancetime", distTimePair.first.toString())
                    Log.d("distancetime", distTimePair.first.toString())
                    data[position].distance = distTimePair.first
                    data[position].duration = distTimePair.second
                    distance.text = distTimePair.first.toString() + " km"
                    duration.text = (distTimePair.second!!/3600).toInt().toString() + " hours"
                } else {
                    Log.d("distancetime", "UserPos is 0.O")
                }

                Glide.with(context).load(url + data[position].image).into(image)

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

    fun getDistanceAndTime(lat1: Double, lon1: Double, lat2: Double, lon2: Double):  Pair<Double? , Double?>  {
        var distance :Double? = null
        var time:Double? = null
        var response: String
        val thread = Thread {
            try {
                val url =
                    URL("https://api.tomtom.com/routing/1/calculateRoute/$lat1,$lon1:$lat2,$lon2/json?key=DGN137adravN52Y5SA1TMXip7GQusRQp")
                val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                conn.setRequestMethod("GET")
                val ins: InputStream = BufferedInputStream(conn.getInputStream())
                response = org.apache.commons.io.IOUtils.toString(ins, "UTF-8")
                val jsonObject = JSONObject(response)
                val array = jsonObject.getJSONArray("routes")
                val firstRoute = array.getJSONObject(0)

                val summary = firstRoute.getJSONObject("summary")
                distance = summary.getInt("lengthInMeters").toDouble() / 1000
                time = (summary.getInt("travelTimeInSeconds") + summary.getInt("trafficDelayInSeconds")).toDouble()
            } catch (e: ProtocolException) {
                e.printStackTrace()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return Pair(distance , time )
    }



    fun updateParkingList(newdata: List<ParkingLot>) {
        data.clear()
        data.addAll(newdata)
        notifyDataSetChanged()
    }

}