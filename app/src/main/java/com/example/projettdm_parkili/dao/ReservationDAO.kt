package com.example.projettdm_parkili.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.projettdm_parkili.models.Reservation

@Dao
interface ReservationDAO {

    @Insert
    fun addReservation(reservation : Reservation)

    @Insert
    fun addReservations(reservations: List<Reservation>)

    @Query("Select * from reservations;")
    fun getReservations() : List<Reservation>

}