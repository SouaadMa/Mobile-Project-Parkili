package com.example.projettdm_parkili

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projettdm_parkili.adapters.ParkingLotsList_Adapter
import com.example.projettdm_parkili.adapters.ReservationsList_Adapter

import com.example.projettdm_parkili.databinding.FragmentNearestParkingsBinding
import com.example.projettdm_parkili.models.OpenSchedule
import com.example.projettdm_parkili.models.ParkingLot
import com.example.projettdm_parkili.retrofit.EndPoint
import com.example.projettdm_parkili.viewModels.ParkingViewModel
import com.example.projettdm_parkili.viewModels.ReservationViewModel
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.location.LocationRequest.PRIORITY_LOW_POWER
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NearestParkingsFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: FragmentNearestParkingsBinding

    var data : List<ParkingLot>? = null
    var schedules : List<OpenSchedule>? = null

    lateinit var adapter: ParkingLotsList_Adapter
    private lateinit var viewmodel : ParkingViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentNearestParkingsBinding.inflate(layoutInflater)
        val view = binding.root

        return view
    }


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.rvParkings
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager

        adapter = ParkingLotsList_Adapter(requireActivity(), onListItemClickedListener = { position -> onListItemClick(position) })
        recyclerView.adapter = adapter

        viewmodel = ViewModelProvider(requireActivity())[ParkingViewModel::class.java]

        addObservers()

        if(hasLocationPermission()) {
            //Toast.makeText(requireContext(), "Rainbow", Toast.LENGTH_SHORT).show()

            val fusedLocationClient= LocationServices.getFusedLocationProviderClient(requireActivity())

            fusedLocationClient.getCurrentLocation(PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener { location ->
                if (location != null) {
                    // Récupérer les données de localisation de l’objet location
                    Toast.makeText(requireContext(), "Successfully got location", Toast.LENGTH_SHORT).show()
                    //Toast.makeText(requireContext(), location.longitude.toString(), Toast.LENGTH_SHORT).show()
                    viewmodel.loadData(location.latitude, location.longitude)
                    adapter.userPos = mutableListOf(location.latitude, location.longitude)

                }
                else {
                    viewmodel.loadData()
                }
            }
            /*fusedLocationClient.getCurrentLocation(PRIORITY_LOW_POWER, null).addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
                viewmodel.loadData()

            }*/
        }
        else {
            //Toast.makeText(requireContext(), "No rainbow", Toast.LENGTH_SHORT).show()
            requestLocationPermission()
            viewmodel.loadData()
        }

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
                            ParkingLotsList_Adapter(ctx, onListItemClickedListener = { position -> onListItemClick(position) })
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

        var bundle = bundleOf("position" to position)
        requireActivity().findNavController(R.id.navHost).navigate(R.id.action_fragmentnearest_to_fragmentdetails, bundle)
    }

    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestLocationPermission() {
        Log.d("perm", "requestlocationpermission")
        EasyPermissions.requestPermissions(
            this,
            "This application cannot work properly without Fine Location permission",
            NavHomeActivity.PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("perm", "onrequestpermissionresult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Log.d("perm", "onpermissiondenied")
        if(EasyPermissions.somePermissionPermanentlyDenied(this, listOf(perms.first()))) {
            SettingsDialog.Builder(requireContext()).build().show()
        }
        else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            requireContext(),
            "Permission Granted",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun addObservers() {
        // loading observer
        viewmodel.loading.observe(requireActivity(), Observer {  loading->
            if(loading) {
                binding.progressBar.visibility = View.VISIBLE
                Log.d("reserv", "loading true")
            }
            else {
                binding.progressBar.visibility = View.GONE
                Log.d("reserv", "loading false")
            }

        })

        // Error message observer
        viewmodel.errorMessage.observe(requireActivity(), Observer {  message ->
            Toast.makeText(requireContext(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
            Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()

        })


        // List reserv observer
        viewmodel.data.observe(requireActivity(), Observer {  data ->
            Log.d("reserv", "data posted")
            adapter.updateParkingList(data)
            Log.d("reserv", "data passed to adapter")
        })

    }
}