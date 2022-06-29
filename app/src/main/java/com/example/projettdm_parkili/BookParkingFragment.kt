package com.example.projettdm_parkili

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.projettdm_parkili.databinding.FragmentBookParkingBinding
import com.example.projettdm_parkili.models.ParkingLot
import com.example.projettdm_parkili.models.Reservation
import com.example.projettdm_parkili.retrofit.EndPoint
import com.example.projettdm_parkili.utils.getUserId
import com.example.projettdm_parkili.validator.EmptyValidator
import com.example.projettdm_parkili.viewModels.ParkingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS


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

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.editTextReservationStartDate.setShowSoftInputOnFocus(false)
        binding.editTextReservationStartDate.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(), { view, year, monthOfYear, dayOfMonth ->
                binding.editTextReservationStartDate.hint = "" + dayOfMonth + " " + (monthOfYear.toInt()+1).toString() + ", " + year
            }, year, month, day)

            dpd.show()
        }

        binding.editTextReservationEndDate.setShowSoftInputOnFocus(false)
        binding.editTextReservationEndDate.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(), { view, year, monthOfYear, dayOfMonth ->
                binding.editTextReservationEndDate.hint = "" + dayOfMonth + " " + monthOfYear+1 + ", " + year
            }, year, month, day)
            dpd.show()
        }

        binding.editTextReservationStartTime.setShowSoftInputOnFocus(false)
        binding.editTextReservationStartTime.setOnClickListener {
            val timePicker: TimePickerDialog = TimePickerDialog(
                requireContext(),
                timePickerDialogListener_entrytime,
                12,
                10,
                false
            )
            timePicker.show()
        }

        binding.editTextReservationEndTime.setShowSoftInputOnFocus(false)
        binding.editTextReservationEndTime.setOnClickListener {
            val timePicker: TimePickerDialog = TimePickerDialog(
                requireContext(),
                timePickerDialogListener_exittime,
                12,
                10,
                false
            )
            timePicker.show()
        }

        binding.textViewEstimatedpricetitle.setOnClickListener {
            estimatePrice()
        }
        binding.textViewEstimatedprice.setOnClickListener {
            estimatePrice()
        }


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



    private val timePickerDialogListener_entrytime: TimePickerDialog.OnTimeSetListener =
        object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

                val formattedTime: String = when {
                    hourOfDay == 0 -> {
                        if (minute < 10) {
                            "${hourOfDay + 12}:0${minute} am"
                        } else {
                            "${hourOfDay + 12}:${minute} am"
                        }
                    }
                    hourOfDay > 12 -> {
                        if (minute < 10) {
                            "${hourOfDay - 12}:0${minute} pm"
                        } else {
                            "${hourOfDay - 12}:${minute} pm"
                        }
                    }
                    hourOfDay == 12 -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute} pm"
                        } else {
                            "${hourOfDay}:${minute} pm"
                        }
                    }
                    else -> {
                        if (minute < 10) {
                            "${hourOfDay}:${minute} am"
                        } else {
                            "${hourOfDay}:${minute} am"
                        }
                    }
                }

                binding.editTextReservationStartDate.hint = formattedTime
            }
        }

    private val timePickerDialogListener_exittime: TimePickerDialog.OnTimeSetListener =
        object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

                // logic to properly handle
                // the picked timings by user
                val formattedTime: String = when {
                    hourOfDay == 0 -> {
                        if (minute < 10) {
                            "${hourOfDay + 12}:0${minute} am"
                        } else {
                            "${hourOfDay + 12}:${minute} am"
                        }
                    }
                    hourOfDay > 12 -> {
                        if (minute < 10) {
                            "${hourOfDay - 12}:0${minute} pm"
                        } else {
                            "${hourOfDay - 12}:${minute} pm"
                        }
                    }
                    hourOfDay == 12 -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute} pm"
                        } else {
                            "${hourOfDay}:${minute} pm"
                        }
                    }
                    else -> {
                        if (minute < 10) {
                            "${hourOfDay}:${minute} am"
                        } else {
                            "${hourOfDay}:${minute} am"
                        }
                    }
                }

                binding.editTextReservationEndDate.hint = formattedTime
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

    fun estimatePrice() {

    }


}