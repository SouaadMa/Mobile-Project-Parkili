package com.example.projettdm_parkili.retrofit

import com.example.projettdm_parkili.models.Review
import com.example.projettdm_parkili.models.User
import com.example.projettdm_parkili.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EndPoint {

    //@FormUrlEncoded
    @POST("reviews/add")
    suspend fun addNewReview(
        @Body review: Review
    ) : Response<Review>

    @POST("users/add")
    suspend fun addNewUser(
        @Body user: User
    ) : Response<User>

    //@GET("users/{username}/{pwd}")
    //suspend fun getUser(@Path("username") username: String?, @Path("pwd") pwd: String?)

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