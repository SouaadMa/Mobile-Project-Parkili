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

    @GET("reviews/{parkingid}")
    suspend fun getAverageRating(
        @Path("parkingid") parkingid : Int
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

    @GET("parkings/nearest/{lat}/{lng}")
    suspend fun getNearestParkings(
        @Path("lat") lat : Double,
        @Path("lng") lng : Double
    ) : Response<List<ParkingLot>>

    @GET("schedules/")
    suspend fun getSchedules(
    ) : Response<List<OpenSchedule>>

    @GET("reservations/{userId}")
    suspend fun getUserReservations(
        @Path("userId") userId : Int
    ) : Response<List<Reservation>>

    @POST("reservations/add")
    suspend fun addNewReservation(
        @Body reservation : Reservation
    ) : Response<Reservation>

    @GET("parkings/{name}")
    suspend fun getParkingsByName(
        @Path("name") name : String
    ): Response<List<ParkingLot>>

    @GET("parkings/{location}")
    suspend fun getParkingsByLocation(
        @Path("location") location : String
    ): Response<List<ParkingLot>>

    @GET("parkings/{location}/{price}/{distance}")
    suspend fun getParkingsByLocationMaxPriceMaxDistance(
        @Path("location") location : String,
        @Path("price") price : Double,
        @Path("distance") distance : Double
    ): Response<List<ParkingLot>>

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