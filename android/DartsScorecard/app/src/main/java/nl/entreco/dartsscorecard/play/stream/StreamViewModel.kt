package nl.entreco.dartsscorecard.play.stream

import android.content.ComponentName
import android.content.ServiceConnection
import android.databinding.ObservableField
import android.os.IBinder
import nl.entreco.dartsscorecard.base.DialogHelper
import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.dartsscorecard.di.streaming.StreamScope
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.dartsscorecard.streaming.StreamingService
import nl.entreco.dartsscorecard.streaming.StreamingServiceListener
import nl.entreco.domain.streaming.*
import nl.entreco.domain.streaming.send.RegisterStreamerRequest
import nl.entreco.domain.streaming.send.RegisterStreamerResponse
import nl.entreco.domain.streaming.send.RegisterStreamerUsecase
import nl.entreco.shared.log.Logger
import org.webrtc.CameraVideoCapturer
import org.webrtc.PeerConnection
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject
import javax.inject.Named

class StreamViewModel @Inject constructor(
        @ApplicationScope private val logger: Logger,
        @ActivityScope private val dialogHelper: DialogHelper,
        @StreamScope @Named("local") private val localVideoView: SurfaceViewRenderer,
        @ApplicationScope private val serviceLauncher: ServiceLauncher,
        private val registerStreamerUsecase: RegisterStreamerUsecase,
        private val disconnectFromSignallingUsecase: DisconnectFromSignallingUsecase
) : StreamingServiceListener {

    val connectionState = ObservableField<ConnectionState>(Unknown)

    private var service: StreamingService? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            onWebRtcServiceConnected((iBinder as (StreamingService.LocalBinder)).service)
            dialogHelper.showStreamDialog{ code ->
                registerStreamerUsecase.go(RegisterStreamerRequest(code),
                        onRegisterSuccess(),
                        onRegisterFailed())
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            onWebRtcServiceDisconnected()
        }
    }

    fun onRegisterSuccess(): (RegisterStreamerResponse) -> Unit = { response ->
        service?.offerDevice(response.uuid)
    }

    fun onRegisterFailed(): (Throwable) -> Unit = { error ->
        serviceLauncher.showError(error.localizedMessage)
    }

    fun attachService() {
        connectionState.set(Initializing)
        serviceLauncher.launchStreamingService(serviceConnection)
    }

    private fun onWebRtcServiceConnected(service: StreamingService) {
        logger.d("Service connected")
        connectionState.set(ReadyToConnect)
        this.service = service
        service.attachLocalView(localVideoView)
        service.attachServiceActionsListener(listener = this)
    }

    private fun onWebRtcServiceDisconnected() {
        logger.d("Service disconnected")
        connectionState.set(Disconnected)
    }

    override fun criticalWebRTCServiceException(throwable: Throwable) {
        unbindService()
        serviceLauncher.criticalWebRTCServiceException(throwable)
    }

    override fun connectionStateChange(iceConnectionState: PeerConnection.IceConnectionState?) {
        serviceLauncher.connectionStateChange(iceConnectionState)
        when (iceConnectionState) {
            PeerConnection.IceConnectionState.CONNECTED -> {
                connectionState.set(Connected)
            }
            PeerConnection.IceConnectionState.DISCONNECTED -> {
//                getView()?.showWillTryToRestartMsg()
                connectionState.set(Disconnected)
            }
            else -> {
                //no-op for now - could show or hide progress bars or messages on given event
            }
        }
    }

    fun onDestroy() {
        service?.let {
            it.detachViews()
            unbindService()
        }
    }

    private fun unbindService() {
        service?.let {
            it.detachServiceActionsListener()
            serviceLauncher.unbindServiceConnection(serviceConnection)
            service = null
        }
    }

    fun switchCamera(switchHandler: CameraVideoCapturer.CameraSwitchHandler) {
        service?.switchCamera(switchHandler)
    }

    fun onStart() {
        service?.hideBackground()
    }

    fun onStop() {
        service?.showBackground()
    }

    fun disconnect() {
        connectionState.set(Disconnecting)
        service?.let {
            it.stopSelf()
            unbindService()
        }
    }
}