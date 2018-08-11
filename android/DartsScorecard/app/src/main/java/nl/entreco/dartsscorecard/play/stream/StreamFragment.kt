package nl.entreco.dartsscorecard.play.stream

import android.content.ComponentName
import android.content.ServiceConnection
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.FragmentStreamBinding
import nl.entreco.dartsscorecard.streaming.StreamingService
import nl.entreco.dartsscorecard.streaming.StreamingServiceListener
import org.webrtc.PeerConnection

class StreamFragment : Fragment(), StreamingServiceListener {

    companion object {
        val TAG: String = StreamFragment::class.java.name
    }

    var service: StreamingService? = null
    private lateinit var binding: FragmentStreamBinding
    private lateinit var serviceConnection: ServiceConnection

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stream, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: If All Permissions Set
        attachService()
    }

    override fun onStart() {
        super.onStart()
        service?.hideBackground()
    }

    override fun onStop() {
        super.onStop()
        if (!activity?.isChangingConfigurations!!) {
            service?.showBackground()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        service?.let {
            it.detachViews()
            unbindService()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        if (remoteVideoView.visibility == View.VISIBLE) {
//            outState.putBoolean(KEY_IN_CHAT, true)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!activity?.isChangingConfigurations!!) disconnect()
    }

    private fun initAlreadyRunningConnection() {
//        showCamViews()
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
                onWebRtcServiceConnected((iBinder as (StreamingService.LocalBinder)).service)
//                getPresenter().listenForDisconnectOrders()
                // Listen for Disconnects
            }

            override fun onServiceDisconnected(componentName: ComponentName) {
                onWebRtcServiceDisconnected()
            }
        }
        startAndBindWebRTCService(serviceConnection)
    }

    fun attachService() {
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
                onWebRtcServiceConnected((iBinder as (StreamingService.LocalBinder)).service)
                // Start Roulette....
            }

            override fun onServiceDisconnected(componentName: ComponentName) {
                onWebRtcServiceDisconnected()
            }
        }
        startAndBindWebRTCService(serviceConnection)
    }

    private fun startAndBindWebRTCService(serviceConnection: ServiceConnection) {
        StreamingService.startService(context!!)
        StreamingService.bindService(context!!, serviceConnection)
    }

    private fun disconnect() {
        service?.let {
            it.stopSelf()
            unbindService()
        }
    }

    private fun unbindService() {
        service?.let {
            it.detachServiceActionsListener()
            context?.unbindService(serviceConnection)
            service = null
        }
    }

    private fun onWebRtcServiceConnected(service: StreamingService) {
//        Log.d("Service connected")
        this.service = service
        service.attachLocalView(binding.localVideoView)
//        service.attachRemoteView(remoteVideoView)
//        syncButtonsState(service)
        service.attachServiceActionsListener(webRtcServiceListener = this)
    }

    private fun onWebRtcServiceDisconnected() {
//        Log.d("Service disconnected")
    }

    override fun criticalWebRTCServiceException(throwable: Throwable) {
        unbindService()
        Toast.makeText(context, "Critical error", Toast.LENGTH_LONG).show()
    }

    override fun connectionStateChange(iceConnectionState: PeerConnection.IceConnectionState) {
        Toast.makeText(context, "Connection State Change", Toast.LENGTH_LONG).show()
    }
}