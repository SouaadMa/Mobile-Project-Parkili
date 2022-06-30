package com.example.projettdm_parkili.models

import java.io.Serializable

data class ParkingLot (

    val parking_id : Int,
    val name : String,
    val commune : String,
    val positionlat : Double,
    val positionlng : Double,
    val nb_occupiedSpots : Int,
    val nb_totalSpots : Int,
    val priceperhour: Double,
    val image : String,
    var distance : Double ?, /**/
    var duration : Double ? /**/

): Serializable
