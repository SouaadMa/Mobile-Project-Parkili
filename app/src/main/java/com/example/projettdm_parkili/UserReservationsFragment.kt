package com.example.projettdm_parkili

import android.content.Context
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
import com.example.projettdm_parkili.adapters.ReservationsList_Adapter
import com.example.projettdm_parkili.databinding.FragmentUserReservationsBinding
import com.example.projettdm_parkili.retrofit.EndPoint
import com.example.projettdm_parkili.utils.getUserId
import com.example.projettdm_parkili.viewModels.ReservationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserReservationsFragment : Fragment() {

    private lateinit var binding: FragmentUserReservationsBinding
    private lateinit var viewmodel : ReservationViewModel

    //var data : List<Reservation>? = null
    lateinit var adapter: ReservationsList_Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentUserReservationsBinding.inflate(layoutInflater)
        val view = binding.root

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.rvReservations
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager

        adapter = ReservationsList_Adapter(requireActivity(), onListItemClickedListener = { position -> onListItemClick(position) } )
        recyclerView.adapter = adapter

        viewmodel = ViewModelProvider(requireActivity())[ReservationViewModel::class.java]
        viewmodel.loadData(requireActivity())


        // add Observers
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
            adapter.updateReservationList(data)
            Log.d("reserv", "data passed to adapter")
        })



        //loadData(recyclerView, requireActivity())


    }

    private fun loadData(recyclerView : RecyclerView, ctx : Context) {

        val eP = EndPoint.createInstance()

        CoroutineScope(Dispatchers.IO).launch {

            val resp = eP.getUserReservations(getUserId(ctx))

            withContext(Dispatchers.Main) {
                if (resp.isSuccessful && resp.body() != null) {

                    //data = resp.body()!!

                    //recyclerView.adapter.

                }

            }

        }
    }

    private fun onListItemClick(position: Int) {
        var id = viewmodel.data.value?.get(position)?.parking_id.toString()
        var bundle = bundleOf("id" to id)
        requireActivity().findNavController(R.id.navHost).navigate(R.id.action_fragment_reservations_to_fragment_qr, bundle)
    }

}