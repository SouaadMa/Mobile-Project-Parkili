package com.example.projettdm_parkili.models

import java.io.Serializable
import java.sql.Time

data class OpenSchedule (

    val parking_id : Int,
    val dayOfWeek : String,
    val openTime : Time,
    val closeTime : Time

): Serializable
