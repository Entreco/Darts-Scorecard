package nl.entreco.dartsscorecard.archive

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.archive.ArchiveServiceLauncher.Companion.EXTRA_GAME_ID
import nl.entreco.dartsscorecard.di.archive.ArchiveModule
import nl.entreco.dartsscorecard.di.service.ServiceModule
import nl.entreco.domain.profile.archive.ArchiveStatsRequest
import nl.entreco.domain.profile.archive.ArchiveStatsResponse
import java.util.concurrent.atomic.AtomicBoolean

class ArchiveJobService : JobService() {

    companion object {
        private const val CHANNEL_ID = "dsc_archive_channel"
        private const val NOTIF_ID = 180
        private const val NOTIF_COLOR = "#A4B5CE"
    }

    private val app by lazy { application as App }
    private val component by lazy { app.appComponent.plus(ServiceModule(this)) }
    private val archiveStatsUsecase by lazy { component.plus(ArchiveModule()).archive() }

    private val isWorking = AtomicBoolean(false)
    private val isCancelled = AtomicBoolean(false)

    private val notificationManager: NotificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    private val notif by lazy {
        NotificationCompat.Builder(baseContext, CHANNEL_ID)
                .setOngoing(true)
                .setContentTitle(getString(R.string.archive_notif_title))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setColor(Color.parseColor(NOTIF_COLOR))
                .setBadgeIconType(R.mipmap.ic_launcher_foreground)
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
            val name = getString(R.string.archive_channel_name)
            val desc = getString(R.string.archive_channel_description)
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
        archiveStatsUsecase.exec(ArchiveStatsRequest(params.extras.getLong(EXTRA_GAME_ID)), onArchiveDone(params), onArchiveFailed(params))
        return isWorking.get()
    }

    private fun onArchiveDone(params: JobParameters): (ArchiveStatsResponse) -> Unit {
        return {
            isWorking.set(false)
            finish(params)
        }
    }

    private fun onArchiveFailed(params: JobParameters): (Throwable) -> Unit {
        return {
            isWorking.set(false)
            finish(params)
        }
    }

    override fun onStopJob(params: JobParameters): Boolean {
        notificationManager.cancel(NOTIF_ID)
        isCancelled.set(true)
        return finish(params)
    }

    private fun finish(params: JobParameters): Boolean {
        notificationManager.cancel(NOTIF_ID)
        val reschedule = isWorking.get()
        jobFinished(params, reschedule)
        return reschedule
    }
}