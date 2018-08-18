package nl.entreco.dartsscorecard.play.stream

import android.content.ComponentName
import android.content.ServiceConnection
import android.databinding.Observable
import android.databinding.ObservableField
import android.os.IBinder
import nl.entreco.dartsscorecard.base.DialogHelper
import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.dartsscorecard.di.streaming.StreamScope
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.dartsscorecard.streaming.StreamingService
import nl.entreco.dartsscorecard.streaming.StreamingServiceListener
import nl.entreco.domain.streaming.*
import nl.entreco.domain.streaming.receive.ListenForDisconnectsUsecase
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
        private val listenForDisconnectsUsecase: ListenForDisconnectsUsecase,
        private val disconnectUsecase: DisconnectUsecase,
        private val disconnectFromSignallingUsecase: DisconnectFromSignallingUsecase,
        private val cameraSwitchHandler: CameraVideoCapturer.CameraSwitchHandler,
        private val listener: StreamFragment.Listener?
) : StreamingServiceListener {

    private var remoteUuid: String? = null
    private val connectionState = ObservableField<ConnectionState>(Unknown)
    private val connectionChange = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            listener?.onConnectionStateChanged(connectionState.get()!!)
        }
    }

    init {
        connectionState.addOnPropertyChangedCallback(connectionChange)
    }

    private var service: StreamingService? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            onWebRtcServiceConnected((iBinder as (StreamingService.LocalBinder)).service)
            dialogHelper.showStreamDialog(onCodeEntered(), onStreamCancelled())
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            onWebRtcServiceDisconnected()
        }
    }

    private fun onStreamCancelled(): () -> Unit = {
        connectionState.set(Killing)
        listener?.onPleaseKillMe()
    }

    private fun onCodeEntered(): (String) -> Unit {
        return { code ->
            connectionState.set(Connecting)
            registerStreamerUsecase.go(RegisterStreamerRequest(code),
                    onRegisterSuccess(),
                    onRegisterFailed())
        }
    }

    private fun onRegisterSuccess(): (RegisterStreamerResponse) -> Unit = { response ->
        remoteUuid = response.uuid
        service?.offerDevice(response.uuid)
        listenForDisconnectsUsecase.go  {
            disconnect()
        }
    }

    private fun onRegisterFailed(): (Throwable) -> Unit = { error ->
        serviceLauncher.showError(error.localizedMessage)
        connectionState.set(Killing)
        listener?.onPleaseKillMe()
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
                disconnect()
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
            connectionState.set(Unknown)
        }
    }

    fun sendDisconnect() {
        connectionState.set(Disconnecting)
        disconnectFromSignallingUsecase.go(DisconnectFromSignallingRequest(remoteUuid)) {
            disconnect()
        }
    }

    fun switchCamera() {
        service?.switchCamera(cameraSwitchHandler)
    }

    fun toggleMic(){
        service?.toggleMicrophone()
    }

    fun sendMessage(msg: String) {
        service?.sendMessage(msg)
    }

    fun onStart() {
        service?.hideBackground()
    }

    fun onStop() {
        service?.showBackground()
    }

    fun disconnect() {
        connectionState.set(Disconnecting)
        disconnectUsecase.go{
            connectionState.set(Disconnected)
            connectionState.set(Unknown)
        }
        service?.let {
            it.stopSelf()
            unbindService()
        }
    }
}