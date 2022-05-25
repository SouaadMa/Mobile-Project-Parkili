package com.example.projettdm_parkili.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(

    val note : Int,
    val comment : String,
    val isSynchronized : Int = 0

) {
    @PrimaryKey(autoGenerate = true)
    var reviewId:Int?=null

}
