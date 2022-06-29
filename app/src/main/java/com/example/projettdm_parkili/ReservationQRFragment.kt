package com.example.projettdm_parkili

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.projettdm_parkili.databinding.FragmentParkingLotDetailsBinding
import com.example.projettdm_parkili.databinding.FragmentReservationQRBinding
import com.example.projettdm_parkili.models.ParkingLot
import com.example.projettdm_parkili.models.Reservation
import com.example.projettdm_parkili.viewModels.ParkingViewModel
import com.example.projettdm_parkili.viewModels.ReservationViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class ReservationQRFragment : Fragment() {
    private lateinit var binding: FragmentReservationQRBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentReservationQRBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        var id = arguments?.getString("id")!!

        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(id, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }

            binding.ivCodeQR.setImageBitmap(bmp)
        } catch (e: WriterException) {
            e.printStackTrace()
        }


    }

}