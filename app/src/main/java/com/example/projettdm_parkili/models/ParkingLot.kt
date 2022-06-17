package com.example.projettdm_parkili.models

import java.io.Serializable

data class ParkingLot (

    val title : String,
    val state : String,
    var occupation : Int,
    val location : String,
    val distance : Double, //Shouldn't be here
    val duration : Int, //Shouldn't be here either
    val image : Int,
    var note : Double,
    var openingTime : String,
    var closingTime : String,
    var pricePerHour : Double

): Serializable
