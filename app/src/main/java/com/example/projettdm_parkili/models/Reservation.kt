package com.example.projettdm_parkili.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "reservations")
data class Reservation(

    @PrimaryKey(autoGenerate = true)
    val reservation_id : Int? = null,
    val user_id : Int,
    val parking_id : Int,
    val paymentMethod : String, /**/
    val parking_name : String, /**/
    val totalPrice : Double, /**/
    val date : String? = null,
    val entrytime : String,
    val exittime : String,
    val spotnum : Int = 0


) : Serializable
