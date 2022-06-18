package com.example.projettdm_parkili.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId:Int?=null,
    val fullname : String?="",
    val email : String,
    val phonenum : String?="",
    val pwd : String
)

