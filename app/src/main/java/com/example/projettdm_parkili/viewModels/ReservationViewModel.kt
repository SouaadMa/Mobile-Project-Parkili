package com.example.projettdm_parkili.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Database
import com.example.projettdm_parkili.AppDatabase
import com.example.projettdm_parkili.models.Reservation
import com.example.projettdm_parkili.retrofit.EndPoint
import com.example.projettdm_parkili.utils.getUserId
import kotlinx.coroutines.*

class ReservationViewModel : ViewModel() {

    val data = MutableLiveData<List<Reservation>>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable.localizedMessage)
    }

    fun loadData(ctx : Context) {
        /*All reservations of connected user*/
        if(data.value == null) {
            var dao = AppDatabase.buildDatabase(ctx)?.getReservationDao()
            data.postValue(dao?.getReservations())
        }
/*
        if(data.value == null) {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = EndPoint.createInstance().getUserReservations(getUserId(ctx))
                withContext(Dispatchers.Main) {
                    if(response.isSuccessful && response.body() != null) {
                        loading.value = false
                        data.postValue(response.body())
                    }
                    else {
                        onError(response.message())

                    }
                }
            }
        }
*/
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }
}