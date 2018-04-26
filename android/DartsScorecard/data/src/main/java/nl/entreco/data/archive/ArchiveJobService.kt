package nl.entreco.data.archive

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import nl.entreco.data.R
import java.util.concurrent.atomic.AtomicBoolean

class ArchiveJobService : JobService() {

    private val CHANNEL_ID = "dsc_archive_channel"
    private val NOTIF_ID = 180
    private val isWorking = AtomicBoolean(false)
    private val isCancelled = AtomicBoolean(false)

    private val notificationManager: NotificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val notif by lazy {
        NotificationCompat.Builder(baseContext, CHANNEL_ID)
                .setOngoing(true)
                .setContentTitle(getString(R.string.notif_title))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setColor(Color.parseColor("#A4B5CE"))
                .setLocalOnly(true)
                .setWhen(System.currentTimeMillis())
                .setUsesChronometer(true)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setProgress(0, 0, true)
    }

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
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartJob(params: JobParameters): Boolean {
        isCancelled.set(false)
        isWorking.set(true)
        notificationManager.notify(NOTIF_ID, notif.build())
        Thread {
            doInBackground(params)
        }.start()
        return isWorking.get()
    }

    override fun onStopJob(params: JobParameters): Boolean {
        notificationManager.cancel(NOTIF_ID)
        isCancelled.set(true)
        return finish(params)
    }

    private fun doInBackground(params: JobParameters) {
        if (isCancelled.get()) {
            return
        }

        // Do Actual work
        SystemClock.sleep(10_000)


        isWorking.set(false)
        finish(params)
    }

    private fun finish(params: JobParameters): Boolean {
        notificationManager.cancel(NOTIF_ID)
        val reschedule = isWorking.get()
        jobFinished(params, reschedule)
        return reschedule
    }
}