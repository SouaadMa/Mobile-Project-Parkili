package com.example.projettdm_parkili.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projettdm_parkili.R
import com.example.projettdm_parkili.models.Reservation
import com.example.projettdm_parkili.models.User
import com.example.projettdm_parkili.url
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter


class ReservationsList_Adapter (
    val context : Context

) : RecyclerView.Adapter<ReservationsList_Adapter.ReservationHolder>() {

    var data = mutableListOf<Reservation>()

    class ReservationHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title = view.findViewById(R.id.tv_parkingtitle) as TextView
        val date = view.findViewById(R.id.tv_datereservation) as TextView
        val entrytime = view.findViewById(R.id.tv_entrytime) as TextView
        val exittime = view.findViewById(R.id.tv_exittime) as TextView
        val totalPrice = view.findViewById(R.id.tv_price) as TextView

        //val image = view.findViewById(R.id.iv_parkingimage) as ImageView
        val qrcode = view.findViewById(R.id.idIVQrcode) as ImageView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationHolder {
        Log.d("reserv", "onCreateViewHolder")

        return ReservationHolder(LayoutInflater.from(context).inflate(R.layout.reservation_item, parent, false))
    }


    override fun onBindViewHolder(holder: ReservationHolder, position: Int) {

        Log.d("reserv", "onBindViewHolder")

        holder.apply {
            title.text = data[position].parking_name
            date.text = data[position].date
            entrytime.text = data[position].entrytime
            exittime.text = data[position].exittime
            totalPrice.text = data[position].totalPrice.toString()



            val writer = QRCodeWriter()
            try {
                val bitMatrix = writer.encode(data[position].reservation_id.toString(), BarcodeFormat.QR_CODE, 512, 512)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }

                qrcode.setImageBitmap(bmp)
            } catch (e: WriterException) {
                e.printStackTrace()
            }

            //image.setImageResource(data[position].image)
            //Glide.with(context).load(url + data[position].image).into(image)

        }
    }

    override fun getItemCount() = data.size

    fun updateReservationList(newdata: List<Reservation>) {
        data.clear()
        data.addAll(newdata)
        notifyDataSetChanged()
    }


}