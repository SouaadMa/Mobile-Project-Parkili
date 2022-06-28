package com.example.projettdm_parkili.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projettdm_parkili.models.ParkingLot
import com.example.projettdm_parkili.retrofit.EndPoint
import com.example.projettdm_parkili.utils.getUserId
import kotlinx.coroutines.*

class ParkingViewModel : ViewModel() {


    val data = MutableLiveData<List<ParkingLot>>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable.localizedMessage)
    }

    fun loadData(ctx : Context) {
        /*Nearest parkings of connected user*/
        if(data.value == null) {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = EndPoint.createInstance().getParkings()
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

    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

}