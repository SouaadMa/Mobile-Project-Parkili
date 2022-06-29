package com.example.projettdm_parkili

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.projettdm_parkili.databinding.FragmentBookParkingBinding
import com.example.projettdm_parkili.databinding.FragmentParkingLotDetailsBinding
import com.example.projettdm_parkili.models.ParkingLot
import com.example.projettdm_parkili.models.Reservation
import com.example.projettdm_parkili.models.User
import com.example.projettdm_parkili.retrofit.EndPoint
import com.example.projettdm_parkili.utils.getUserId
import com.example.projettdm_parkili.validator.EmailValidator
import com.example.projettdm_parkili.validator.EmptyValidator
import com.example.projettdm_parkili.viewModels.ParkingViewModel
import kotlinx.coroutines.*
import retrofit2.Response

class BookParkingFragment : Fragment() {
    private lateinit var binding: FragmentBookParkingBinding

    var data : ParkingLot? = null
    var position = 0

    var price = 0.0

    private var db : AppDatabase? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentBookParkingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        position = arguments?.getInt("position")!!
        data = ViewModelProvider(requireActivity())[ParkingViewModel::class.java].data.value?.get(position)
        db = AppDatabase.buildDatabase(requireActivity())

        fillTextViews()


        binding.buttonCancelbook.setOnClickListener{
            requireActivity().findNavController(R.id.navHost).popBackStack()
        }

        binding.buttonConfirmbook.setOnClickListener {

            val startdate = binding.editTextReservationStartDate.text.toString()
            val startdateEmptyValidation = EmptyValidator(startdate).validate()
            val enddate = binding.editTextReservationEndDate.text.toString()
            val enddateEmptyValidation = EmptyValidator(enddate).validate()

            var errormsg =
                if (!startdateEmptyValidation.isSuccess)
                    startdateEmptyValidation.message
                else if (!enddateEmptyValidation.isSuccess)
                    enddateEmptyValidation.message
                else null

            if(errormsg != null) Toast.makeText(requireActivity(), errormsg, Toast.LENGTH_SHORT).show()
            else {
                price = data?.pricePerHour!! * 2
                CoroutineScope(Dispatchers.IO).launch {
                    var response = bookParking()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            requireActivity().findNavController(R.id.navHost)
                                .navigate(R.id.action_fragment_booking_to_fragment_reservations)

                        } else {
                            Log.d("create-res", response.message())
                            Toast.makeText(
                                requireActivity(),
                                "Reservation was not created",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

        }

    }

    fun fillTextViews() {
        binding.textViewParkinglotname.text = data?.name
        binding.textViewParkinglotlocation.text = data?.commune

    }

    suspend fun bookParking() : Response<Reservation> {
        val resp = EndPoint.createInstance().addNewReservation(
            Reservation(
                user_id = getUserId(requireContext()),
                parking_id = data?.parking_id!!,
                entrytime = binding.editTextReservationStartDate.text.toString(),
                exittime = binding.editTextReservationEndDate.text.toString(),
                paymentMethod = binding.editTextPaymentMethod.text.toString(),
                totalPrice = price,
                parking_name = binding.textViewParkinglotname.text.toString()
            )
        )
        return resp
    }
}