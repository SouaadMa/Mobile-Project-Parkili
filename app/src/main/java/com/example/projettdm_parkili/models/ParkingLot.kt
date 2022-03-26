package com.example.projettdm_parkili.models

data class ParkingLot(
    val title : String,
    val state : String,
    val occupation : Int,
    val location : String,
    val distance : Double,
    val duration : Int,
    val image : Int
)
