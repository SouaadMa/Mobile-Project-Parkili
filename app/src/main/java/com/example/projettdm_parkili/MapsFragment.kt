package com.example.projettdm_parkili

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.projettdm_parkili.viewModels.ParkingViewModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class MapsFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var viewmodel : ParkingViewModel
    private lateinit var position : LatLng

    private lateinit var thisMap : GoogleMap

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        thisMap = googleMap
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        viewmodel = ViewModelProvider(requireActivity())[ParkingViewModel::class.java]

        if(hasLocationPermission()) {
            //Toast.makeText(requireContext(), "Rainbow", Toast.LENGTH_SHORT).show()

            val fusedLocationClient= LocationServices.getFusedLocationProviderClient(requireActivity())

            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener { location ->
                if (location != null) {
                    // Récupérer les données de localisation de l’objet location
                    Toast.makeText(requireContext(), "Successfully got location", Toast.LENGTH_SHORT).show()
                    //Toast.makeText(requireContext(), location.longitude.toString(), Toast.LENGTH_SHORT).show()
                    //viewmodel.loadData(location.latitude, location.longitude)
                    position = LatLng(location.latitude, location.longitude)
                    googleMap.addMarker(MarkerOptions().position(position).title("My position"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))

                    addOtherMarkers()

                }
                else {
                    googleMap.addMarker(MarkerOptions().position(sydney).title("My position"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

                }
            }
        }
        else {
            //Toast.makeText(requireContext(), "No rainbow", Toast.LENGTH_SHORT).show()
            requestLocationPermission()
            googleMap.addMarker(MarkerOptions().position(sydney).title("My position"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
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

    fun addOtherMarkers() {
        for (parking in viewmodel.data.value!!) {
            thisMap.addMarker(MarkerOptions().position(LatLng(parking.positionlat, parking.positionlng)).title(parking.name))
        }
    }
}
