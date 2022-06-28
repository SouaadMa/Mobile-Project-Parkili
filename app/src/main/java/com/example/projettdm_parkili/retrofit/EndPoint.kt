package com.example.projettdm_parkili.retrofit

import com.example.projettdm_parkili.models.*
import com.example.projettdm_parkili.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

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

    @POST("users/login")
    suspend fun login(
        @Body user: User
    ) : Response<User>

    @GET("parkings/")
    suspend fun getParkings(
    ) : Response<List<ParkingLot>>

    @GET("parkings/nearest/{userId}")
    suspend fun getNearestParkings(
        @Path("userId") userId : Int
    ) : Response<List<ParkingLot>>

    @GET("schedules/")
    suspend fun getSchedules(
    ) : Response<List<OpenSchedule>>

    @GET("reservations/{userId}")
    suspend fun getUserReservations(
        @Path("userId") userId : Int
    ) : Response<List<Reservation>>

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