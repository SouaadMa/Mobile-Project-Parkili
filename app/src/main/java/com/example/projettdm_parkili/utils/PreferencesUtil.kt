package com.example.projettdm_parkili.utils

import android.content.Context
import android.location.Location
import androidx.core.content.edit

const val fileName = "parkili_pref"
const val invalidUserId = -1
const val invalidUserName = "fullname"

fun saveUserID(context: Context,userId:Int) {
    val pref = context.getSharedPreferences(fileName,Context.MODE_PRIVATE)
    pref.edit {
        putInt("userId",userId)
    }
}

fun getUserId(context: Context):Int {
    val pref = context.getSharedPreferences(fileName,Context.MODE_PRIVATE)
    return pref.getInt("userId",invalidUserId)
}

fun saveUserName(context: Context,name:String) {
    val pref = context.getSharedPreferences(fileName,Context.MODE_PRIVATE)
    pref.edit {
        putString("userName", name)
    }
}

fun getUserName(context: Context):String {
    val pref = context.getSharedPreferences(fileName,Context.MODE_PRIVATE)
    return pref.getString("userName", invalidUserName)!!
}

fun isLogin(context: Context)= getUserId(context)!=invalidUserId

fun clearUserId(context: Context) {
    saveUserID(context,invalidUserId)
}

