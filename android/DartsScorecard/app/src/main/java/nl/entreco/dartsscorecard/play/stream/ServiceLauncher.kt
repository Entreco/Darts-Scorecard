package nl.entreco.dartsscorecard.play.stream

import android.content.Context
import android.content.ServiceConnection
import android.widget.Toast
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.dartsscorecard.streaming.StreamingService
import org.webrtc.PeerConnection
import javax.inject.Inject

class ServiceLauncher @Inject constructor(@ActivityScope private val context: Context) {


    fun launchStreamingService(serviceConnection: ServiceConnection) {
        StreamingService.startService(context)
        StreamingService.bindService(context, serviceConnection)
    }

    fun unbindServiceConnection(serviceConnection: ServiceConnection) {
        context.unbindService(serviceConnection)
    }

    fun criticalWebRTCServiceException(throwable: Throwable) {
        Toast.makeText(context, "Critical error", Toast.LENGTH_LONG).show()
    }

    fun connectionStateChange(iceConnectionState: PeerConnection.IceConnectionState?) {
        Toast.makeText(context, "Connection State Change", Toast.LENGTH_LONG).show()
    }

    fun showError(msg: String){
        Toast.makeText(context, "Error: $msg", Toast.LENGTH_LONG).show()
    }
}