package com.example.projettdm_parkili.models

import java.io.Serializable

data class Reservation(

    val reservation_id : Int,
    val user_id : Int,
    val parking_id : Int,
    val parking_name : String, /**/
    val totalPrice : Double, /**/
    val date : String,
    val entrytime : String,
    val exittime : String,
    val spotnum : Int,
    val rating : Int


) : Serializable
