package nl.entreco.dartsscorecard.tv.launch

import android.content.ComponentName
import android.content.ServiceConnection
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.IBinder
import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.dartsscorecard.di.tv.TvScope
import nl.entreco.dartsscorecard.play.stream.ServiceLauncher
import nl.entreco.dartsscorecard.streaming.ReceiverService
import nl.entreco.dartsscorecard.streaming.ReceivingServiceListener
import nl.entreco.domain.streaming.*
import nl.entreco.domain.streaming.receive.ListenForDisconnectsUsecase
import nl.entreco.domain.streaming.receive.RegisterReceiverRequest
import nl.entreco.domain.streaming.receive.RegisterReceiverResponse
import nl.entreco.domain.streaming.receive.RegisterReceiverUsecase
import nl.entreco.shared.log.Logger
import org.webrtc.PeerConnection
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject
import javax.inject.Named

class LaunchTvViewModel @Inject constructor(
        @ApplicationScope private val logger: Logger,
        @TvScope @Named("remote") private val remoteVideoView: SurfaceViewRenderer,
        private val disconnectUsecase: DisconnectUsecase,
        private val listenForDisconnectsUsecase: ListenForDisconnectsUsecase,
        private val registerReceiverUsecase: RegisterReceiverUsecase,
        @ApplicationScope private val serviceLauncher: ServiceLauncher
) : ReceivingServiceListener {

    val isLoading = ObservableBoolean(false)
    val isStreaming = ObservableBoolean(false)
    val registrationCode = ObservableField<String>("")
    val connectionState = ObservableField<ConnectionState>(Unknown)

    init {
        isLoading.set(true)
        registerReceiverUsecase.go(RegisterReceiverRequest("todo -> maybe tv or some identifier"),
                registrationOk(), registrationFailed())
    }

    private fun registrationOk(): (RegisterReceiverResponse) -> Unit = { response ->
        listenForDisconnectOrders()
        registrationCode.set(response.code)
        isLoading.set(false)
        attachService()
    }

    private fun registrationFailed(): (Throwable) -> Unit = {
        isLoading.set(false)
        connectionState.set(Killing)
    }

    private var service: ReceiverService? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            onWebRtcServiceConnected((iBinder as (ReceiverService.LocalBinder)).service)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            onWebRtcServiceDisconnected()
        }
    }

    private fun listenForDisconnectOrders() {
        listenForDisconnectsUsecase.go {
            disconnect()
            isLoading.set(true)
            registerReceiverUsecase.go(RegisterReceiverRequest("todo -> maybe tv or some identifier"),
                    registrationOk(), registrationFailed())
        }
    }

    private fun attachService() {
        serviceLauncher.launchReceiverService(serviceConnection)
    }

    private fun onWebRtcServiceConnected(service: ReceiverService) {
        logger.d("Service connected")
        connectionState.set(ReadyToConnect)
        this.service = service
        service.attachRemoteView(remoteVideoView)
        service.attachServiceActionsListener(listener = this)
    }

    private fun onWebRtcServiceDisconnected() {
        logger.d("Service disconnected")
        connectionState.set(Disconnected)
        isStreaming.set(false)
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
                isStreaming.set(true)
            }
            PeerConnection.IceConnectionState.DISCONNECTED -> {
//                getView()?.showWillTryToRestartMsg()
                isStreaming.set(false)
                connectionState.set(Disconnected)
                service?.detachViews()
            }
            else -> {
                //no-op for now - could show or hide progress bars or messages on given event
            }
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
        }
        service?.let {
            it.stopSelf()
            it.detachViews()
            unbindService()
        }
    }
}