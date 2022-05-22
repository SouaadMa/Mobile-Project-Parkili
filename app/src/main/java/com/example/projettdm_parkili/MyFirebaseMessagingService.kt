package com.example.projettdm_parkili

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channel_id = "notificationChannel"
const val channel_name = "com.example.projettdm_parkili"

class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(message: RemoteMessage) {
        if(message.getNotification() != null) {
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }


    // Attach the notif generated with the custom layout
    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title : String, message : String) : RemoteViews {
        val remoteView = RemoteViews("com.example.projettdm_parkili", R.layout.notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.desc, message)
        remoteView.setImageViewResource(R.id.notif_image, R.drawable.ic_empty_star)

        return remoteView
    }

    // Generate notif
    fun generateNotification(title : String, message : String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        // Channel id, channel name
        var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channel_id)
            .setSmallIcon(R.drawable.ic_empty_star)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())

    }

    // Show the notif


}