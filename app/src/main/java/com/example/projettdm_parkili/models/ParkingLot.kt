package com.example.projettdm_parkili.models

import java.io.Serializable

data class ParkingLot (

    val parking_id : Int,
    val name : String,
    val commune : String,
    val positionLat : Double,
    val positionLng : Double,
    val nb_occupiedSpots : Int,
    val nb_totalSpots : Int,
    val pricePerHour: Double,
    val image : String

): Serializable
