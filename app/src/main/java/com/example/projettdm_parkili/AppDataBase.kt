package com.example.projettdm_parkili

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projettdm_parkili.dao.ParkingDAO
import com.example.projettdm_parkili.dao.ReviewDAO
import com.example.projettdm_parkili.models.Review

@Database(entities = [Review::class],version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getReviewDao(): ReviewDAO
    abstract fun getParkingDao(): ParkingDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun buildDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) { synchronized(this) {
                INSTANCE = Room.databaseBuilder(context,AppDatabase::class.java,"parkili_db").allowMainThreadQueries().build() } }
            return INSTANCE }
    }
}