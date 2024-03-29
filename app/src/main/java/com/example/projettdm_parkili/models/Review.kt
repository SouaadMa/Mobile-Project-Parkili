package com.example.projettdm_parkili.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true)
    var reviewId:Int?=null,
    val user_id : Int?,
    val parking_id : Int?,
    val note : Int,
    val comment : String?,
    val isSynchronized : Int? = 0
)

