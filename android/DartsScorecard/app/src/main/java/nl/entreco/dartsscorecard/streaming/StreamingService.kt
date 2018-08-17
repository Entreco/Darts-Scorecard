package nl.entreco.dartsscorecard.streaming

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.di.service.ServiceModule
import nl.entreco.dartsscorecard.di.streaming.StreamingModule
import org.webrtc.CameraVideoCapturer
import org.webrtc.SurfaceViewRenderer

class StreamingService : Service() {

    companion object {
        private const val CHANNEL_ID = "dsc_streaming_channel"
        private const val NOTIF_ID = 501
        private const val NOTIF_COLOR = "#A4B5CE"

        fun startService(packageContext: Context) {
            packageContext.startService(Intent(packageContext, StreamingService::class.java))
        }

        fun bindService(context: Context, connection: ServiceConnection) {
            context.bindService(Intent(context, StreamingService::class.java), connection, 0)
        }
    }

    private val app by lazy { application as App }
    private val component by lazy { app.appComponent.plus(ServiceModule(this)) }
    private val streamingController: StreamingController by lazy { component.plus(StreamingModule()).streamingController() }
    private val binder = LocalBinder()

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val notif by lazy {
        NotificationCompat.Builder(baseContext, CHANNEL_ID)
                .setOngoing(true)
                .setContentTitle(getString(R.string.streaming_notif_title))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setColor(Color.parseColor(NOTIF_COLOR))
                .setBadgeIconType(R.mipmap.ic_launcher_foreground)
                .setLocalOnly(true)
                .setWhen(System.currentTimeMillis())
                .setUsesChronometer(true)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setPriority(NotificationCompat.PRIORITY_LOW)
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        streamingController.attachService(this)
    }

    override fun onDestroy() {
        streamingController.detachService()
        hideBackground()
        super.onDestroy()
    }

    fun attachServiceActionsListener(listener: StreamingServiceListener) {
        streamingController.serviceListener = listener
    }

    fun detachServiceActionsListener() {
        streamingController.serviceListener = null
    }

    fun offerDevice(deviceUuid: String) {
        streamingController.offerDevice(deviceUuid)
    }

    fun attachLocalView(localView: SurfaceViewRenderer) {
        streamingController.attachLocalView(localView)
    }

    fun onStop() {
        hideBackground()
        stopSelf()
    }

    fun detachViews() {
        streamingController.detachViews()
    }

    fun switchCamera(
            handler: CameraVideoCapturer.CameraSwitchHandler? = null) = streamingController.switchCamera(
            handler)

    fun showBackground() {
        registerChannel()
        notificationManager.notify(NOTIF_ID, notif.build())
    }

    fun hideBackground() {
        notificationManager.cancel(NOTIF_ID)
    }

    private fun registerChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.streaming_channel_name)
            val desc = getString(R.string.streaming_channel_description)
            val importance = NotificationManager.IMPORTANCE_MIN
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = desc
            // Register channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }

    inner class LocalBinder : Binder() {
        val service: StreamingService
            get() = this@StreamingService
    }
}