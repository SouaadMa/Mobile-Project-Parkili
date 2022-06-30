package com.example.projettdm_parkili

import android.os.Bundle
import android.view.KeyEvent
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
import com.example.projettdm_parkili.adapters.ParkingLotsList_Adapter
import com.example.projettdm_parkili.databinding.FragmentSearchBinding
import com.example.projettdm_parkili.models.ParkingLot
import com.example.projettdm_parkili.viewModels.ParkingViewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    var data : List<ParkingLot>? = null

    lateinit var adapter: ParkingLotsList_Adapter
    private lateinit var viewmodel : ParkingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.rvSearchparkings
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager

        adapter = ParkingLotsList_Adapter(requireActivity(), onListItemClickedListener = { position -> onListItemClick(position) })
        recyclerView.adapter = adapter

        viewmodel = ViewModelProvider(requireActivity())[ParkingViewModel::class.java]

        addObservers()

        binding.editTextSearchLocation.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                if (binding.editTextSearchLocation.text.isNotBlank() and binding.editTextSearchPrice.text.isBlank() and binding.editTextSearchDistance.text.isBlank())
                    viewmodel.searchByLocation(binding.editTextSearchLocation.text.toString())
                else if (binding.editTextSearchLocation.text.isNotBlank() and binding.editTextSearchPrice.text.isNotBlank() and binding.editTextSearchDistance.text.isNotBlank())
                    viewmodel.searchByLocationMaxPriceMaxDistance(binding.editTextSearchLocation.text.toString(), binding.editTextSearchPrice.text.toString().toDouble(), binding.editTextSearchDistance.text.toString().toDouble())

                return@OnKeyListener true
            }
            false
        })
        binding.editTextSearchPrice.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                if (binding.editTextSearchLocation.text.isNotBlank() and binding.editTextSearchPrice.text.isNotBlank() and binding.editTextSearchDistance.text.isNotBlank()) {
                    viewmodel.searchByLocationMaxPriceMaxDistance(binding.editTextSearchLocation.text.toString(), binding.editTextSearchPrice.text.toString().toDouble(), binding.editTextSearchDistance.text.toString().toDouble())
                }

                return@OnKeyListener true
            }
            false
        })


    }

    fun addObservers() {

        // loading observer
        viewmodel.loading.observe(requireActivity(), Observer {  loading->
            if(loading) {
                binding.progressBar.visibility = View.VISIBLE
            }
            else {
                binding.progressBar.visibility = View.GONE
            }

        })

        // Error message observer
        viewmodel.errorMessage.observe(requireActivity(), Observer {  message ->
            Toast.makeText(requireContext(),"Une erreur s'est produite",Toast.LENGTH_SHORT).show()
            Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()

        })

        // List reserv observer
        viewmodel.data.observe(requireActivity(), Observer {  data ->
            adapter.updateParkingList(data)
        })


    }

    private fun onListItemClick(position: Int) {
        Toast.makeText(requireActivity(), data?.get(position)?.name, Toast.LENGTH_SHORT).show()

        var bundle = bundleOf("position" to position)
        requireActivity().findNavController(R.id.navHost).navigate(R.id.action_fragment_search_to_fragment_details, bundle)
    }


}