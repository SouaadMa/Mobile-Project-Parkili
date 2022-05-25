package com.example.projettdm_parkili.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.projettdm_parkili.models.Review

@Dao
interface ReviewDAO {

    @Insert
    fun addReview(review : Review)

    @Query("Select * from reviews where isSynchronized = 0;")
    fun getReviewsToSynchronize() : List<Review>

    @Update
    fun updateReviews(reviews : List<Review>?)


}