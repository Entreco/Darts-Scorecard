package nl.entreco.dartsscorecard.play.stream

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.BaseFragment
import nl.entreco.dartsscorecard.base.PermissionHelper
import nl.entreco.dartsscorecard.databinding.FragmentStreamBinding
import nl.entreco.dartsscorecard.di.streaming.StreamModule
import nl.entreco.domain.streaming.ConnectionState
import org.webrtc.CameraVideoCapturer
import java.util.concurrent.atomic.AtomicBoolean

class StreamFragment : BaseFragment(), StreamController, CameraVideoCapturer.CameraSwitchHandler {

    companion object {
        val TAG: String = StreamFragment::class.java.name
    }

    interface Listener {
        fun onPleaseKillMe()
        fun onConnectionStateChanged(connectionState: ConnectionState)
    }

    private lateinit var binding: FragmentStreamBinding
    private val component by componentProvider {
        it.plus(StreamModule(this, activityListener, binding.localVideoView))
    }
    private val viewModel by lazy { component.viewModel() }
    private val permissionHelper by lazy { component.permissionHelper() }

    private val isShowingFrontCamera = AtomicBoolean(true)
    private var activityListener: StreamFragment.Listener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityListener = context as Listener
    }

    override fun onDetach() {
        super.onDetach()
        activityListener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stream, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionHelper.checkStreamingPermissionsAndConnect { viewModel.attachService() }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) = when (requestCode) {
        PermissionHelper.CHECK_PERMISSIONS_AND_CONNECT_REQUEST_CODE -> permissionHelper.onStreamingPermissionResult(
                grantResults) { viewModel.attachService() }
        else -> error("Unknown permission request code $requestCode")
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
        if (!activity?.isChangingConfigurations!!) {
            viewModel.onStop()
            viewModel.disconnect()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDestroy()
        viewModel.disconnect()
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        if (remoteVideoView.visibility == View.VISIBLE) {
//            outState.putBoolean(KEY_IN_CHAT, true)
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        if (!activity?.isChangingConfigurations!!) viewModel.disconnect()
    }

//    private fun initAlreadyRunningConnection() {
////        showCamViews()
//        serviceConnection = object : ServiceConnection {
//            override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
//                onWebRtcServiceConnected((iBinder as (StreamingService.LocalBinder)).service)
////                getPresenter().listenForDisconnectOrders()
//                // Listen for Disconnects
//            }
//
//            override fun onServiceDisconnected(componentName: ComponentName) {
//                onWebRtcServiceDisconnected()
//            }
//        }
//        startAndBindWebRTCService(serviceConnection)
//    }

    override fun sendDisconnect() {
        viewModel.sendDisconnect()
    }

    override fun toggleCamera() {
        viewModel.switchCamera()
    }

    override fun onCameraSwitchDone(p0: Boolean) {
        isShowingFrontCamera.set(!isShowingFrontCamera.get())
        activity?.invalidateOptionsMenu()

    }

    override fun onCameraSwitchError(p0: String?) {
        activity?.invalidateOptionsMenu()
    }
}