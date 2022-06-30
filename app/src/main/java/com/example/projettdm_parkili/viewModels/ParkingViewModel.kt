package com.example.projettdm_parkili.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projettdm_parkili.models.ParkingLot
import com.example.projettdm_parkili.retrofit.EndPoint
import kotlinx.coroutines.*

class ParkingViewModel : ViewModel() {

    val data = MutableLiveData<List<ParkingLot>>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable.localizedMessage)
    }

    fun loadData(lat : Double, lng : Double) {
        /*Nearest parkings of connected user*/
        if(data.value == null) {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = EndPoint.createInstance().getNearestParkings(lat, lng)
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

    fun loadData() {
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

    fun searchByName(name : String) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = EndPoint.createInstance().getParkingsByName(name)
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

    fun searchByLocation(location : String) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = EndPoint.createInstance().getParkingsByLocation(location)
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

    fun searchByLocationMaxPriceMaxDistance(location : String, maxprice : Double, maxDistance : Double) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = EndPoint.createInstance().getParkingsByLocationMaxPriceMaxDistance(location, maxprice, maxDistance)
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

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

}