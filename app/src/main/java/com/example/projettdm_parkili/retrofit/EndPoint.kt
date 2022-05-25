package com.example.projettdm_parkili.retrofit

import com.example.projettdm_parkili.models.Review
import com.example.projettdm_parkili.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface EndPoint {

    //@FormUrlEncoded
    @POST("reviews/add")
    suspend fun addNewReview(
        @Body review: Review
    ) : Response<Review>

    companion object {
        @Volatile
        var endpoint: EndPoint? = null
        fun createInstance(): EndPoint {
            if(endpoint ==null) {
                synchronized(this) {
                    endpoint = Retrofit.Builder().baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create()).build()
                        .create(EndPoint::class.java)
                }
            }
            return endpoint!!
        }
    }


}