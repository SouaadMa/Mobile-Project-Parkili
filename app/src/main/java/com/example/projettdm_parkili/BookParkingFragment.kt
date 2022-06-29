package com.example.projettdm_parkili

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
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
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


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
                var padding_month = if (monthOfYear+1 < 10)  "0" else ""
                var padding_daymonth = if (dayOfMonth < 10)  "0" else ""
                binding.editTextReservationStartDate.hint = year.toString() + "-" + padding_month + (monthOfYear+1).toString() + "-" + padding_daymonth + dayOfMonth.toString()
            }, year, month, day)

            dpd.show()
        }

        binding.editTextReservationEndDate.setShowSoftInputOnFocus(false)
        binding.editTextReservationEndDate.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(), { view, year, monthOfYear, dayOfMonth ->
                var padding_month = if (monthOfYear+1 < 10)  "0" else ""
                var padding_daymonth = if (dayOfMonth < 10)  "0" else ""
                binding.editTextReservationEndDate.hint = year.toString() + "-" + padding_month + (monthOfYear+1).toString() + "-" + padding_daymonth + dayOfMonth.toString()
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
                true
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
                true
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


                var errormsg =
                    if (binding.editTextReservationStartDate.hint.equals("Entry Date"))
                        "Entry date required but empty"
                    else if (binding.editTextReservationStartTime.hint.equals("Entry Time"))
                        "Entry time required but empty"
                    else if (binding.editTextReservationEndDate.hint.equals("Exit Date"))
                        "Exit date required but empty"
                    else null

                if(errormsg != null) Toast.makeText(requireActivity(), errormsg, Toast.LENGTH_SHORT).show()
                else {

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
                    hourOfDay < 10 -> {
                        if (minute < 10) {
                            "0${hourOfDay}:0${minute}"
                        } else {
                            "0${hourOfDay}:${minute}"
                        }
                    }
                    else -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute}"
                        } else {
                            "${hourOfDay}:${minute}"
                        }
                    }
                }

                binding.editTextReservationStartTime.hint = formattedTime
            }
        }

    private val timePickerDialogListener_exittime: TimePickerDialog.OnTimeSetListener =
        object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

                val formattedTime: String = when {
                    hourOfDay < 10 -> {
                        if (minute < 10) {
                            "0${hourOfDay}:0${minute}"
                        } else {
                            "0${hourOfDay}:${minute}"
                        }
                    }
                    else -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute}"
                        } else {
                            "${hourOfDay}:${minute}"
                        }
                    }
                }

                binding.editTextReservationEndTime.hint = formattedTime
            }
        }

    fun fillTextViews() {
        binding.textViewParkinglotname.text = data?.name
        binding.textViewParkinglotlocation.text = data?.commune

        Glide.with(requireActivity()).load(url + data?.image).into(binding.ivParkingimage)

/*
        val today = Date()
        var padding_month = if (today.month < 10)  "0" else ""
        var padding_daymonth = if (today.day < 10)  "0" else ""
        binding.editTextReservationStartDate.hint = today.year.toString() + "-" + padding_month + today.month.toString() + "-" + padding_daymonth + today.day.toString()
        binding.editTextReservationEndDate.hint = today.year.toString() + "-" + padding_month + today.month.toString() + "-" + padding_daymonth + today.day.toString()
*/
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

        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd HH:mm"
        )

        val start = binding.editTextReservationStartDate.hint.toString() + " " + binding.editTextReservationStartTime.hint.toString()
        val end = binding.editTextReservationEndDate.hint.toString() + " " + binding.editTextReservationEndTime.hint.toString()

        Log.d("time", start)
        Log.d("time", end)

        var today = Date()


        try {
            val oldDate = dateFormat.parse(start)
            System.out.println(oldDate)
            val currentDate = dateFormat.parse(end)
            val diff = currentDate.time - oldDate.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            Log.d("time", hours.toString())
            val days = hours / 24
            if (oldDate.before(today)) {
                Toast.makeText(requireActivity(), "Entry date needs to be after current date", Toast.LENGTH_LONG).show()
                return
            }
            if (currentDate.before(oldDate)) {
                Toast.makeText(requireActivity(), "Entry date needs to be before exit date", Toast.LENGTH_LONG).show()
                Log.e("oldDate", "is previous date")
                Log.e(
                    "Difference: ", " seconds: " + seconds + " minutes: " + minutes
                            + " hours: " + hours + " days: " + days
                )
                return
            }
            else {
                price = hours * data?.priceperhour!!
                binding.textViewEstimatedprice.text = price.toString() + " DA"
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }



    }


}