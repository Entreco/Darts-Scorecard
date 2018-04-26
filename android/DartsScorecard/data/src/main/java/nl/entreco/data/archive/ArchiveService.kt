package nl.entreco.data.archive

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import nl.entreco.data.R


class ArchiveService : IntentService("ArchiveService") {

    private val CHANNEL_ID = "dsc_archive_channel"
    private val NOTIF_ID = 180

    override fun onCreate() {
        registerChannel()
        super.onCreate()
    }

    private fun registerChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val desc = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = desc
            // Register channel with the system
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private val notif by lazy {
        NotificationCompat.Builder(baseContext, CHANNEL_ID)
                .setOngoing(true)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setProgress(0, 0, true)
                .build()
    }

    override fun onHandleIntent(intent: Intent?) {
        startForeground(NOTIF_ID, notif)

        try {
            doInBackground()
        } finally {
            stopForeground(true)
        }
    }

    private fun doInBackground() {
        SystemClock.sleep(10_000)
    }
}