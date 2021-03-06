package com.example.a24friend.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.a24friend.R
import com.example.a24friend.database.getDatabase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {

    // TOPIC must be same as one in the cloud function
    private val TOPIC = "default"
    // TODO perhaps it will not work when the activity collapsed
    var mUserId = "ePtJlyB2JGyugUoRqICN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: this code is to see token
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("MainActivity", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                Log.d("MainActivity", "Token: $token")
            })

        createChannel(
            applicationContext.getString(R.string.match_notification_channel_id),
            applicationContext.getString(R.string.match_notification_channel_name))

        subscribeTopic()
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // change importance
                NotificationManager.IMPORTANCE_HIGH
            )
                // enable badges for this channel
                .apply {
                    setShowBadge(true)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description =
                getString(R.string.match_notification_channel_description)

            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun subscribeTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
            .addOnCompleteListener { task ->
                var message = "Successfully subscribe topic ${TOPIC}."
                if (!task.isSuccessful) {
                    message = "failed to subscribe topic ${TOPIC}."
                }
                Log.d("subscribeTopic", message)
            }
    }
}
