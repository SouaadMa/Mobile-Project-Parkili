package com.example.projettdm_parkili.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.projettdm_parkili.AppDatabase
import com.example.projettdm_parkili.retrofit.EndPoint

class ReviewSyncService(val ctx: Context, val parameters: WorkerParameters): CoroutineWorker(ctx, parameters) {
    override suspend fun doWork(): Result {
        val reviewsToSync = AppDatabase.buildDatabase(ctx)?.getReviewDao()?.getReviewsToSynchronize()
        var result = Result.failure()
        // Insert in server db
        if (reviewsToSync != null) {
            for (review in reviewsToSync) {
                val resp = EndPoint.createInstance().addNewReview(review)
                if(resp.isSuccessful) {
                    result = Result.success()
                }
            }
        }

        val newreviews = reviewsToSync?.map {it.copy(isSynchronized = 1)}
        // Update list to be synchronized
        AppDatabase.buildDatabase(ctx)?.getReviewDao()?.updateReviews(newreviews)

        return result
    }
}