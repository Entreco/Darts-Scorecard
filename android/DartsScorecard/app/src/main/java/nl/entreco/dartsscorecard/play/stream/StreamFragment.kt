package nl.entreco.dartsscorecard.play.stream

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.Snackbar
import android.view.*
import android.widget.Toast
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.areAllPermissionsGranted
import nl.entreco.dartsscorecard.base.BaseFragment
import nl.entreco.dartsscorecard.databinding.FragmentStreamBinding
import nl.entreco.dartsscorecard.di.streaming.StreamModule
import nl.entreco.dartsscorecard.startAppSettings
import nl.entreco.dartsscorecard.streaming.StreamingService
import nl.entreco.dartsscorecard.streaming.StreamingServiceListener
import org.webrtc.CameraVideoCapturer
import org.webrtc.PeerConnection
import java.util.concurrent.atomic.AtomicBoolean

class StreamFragment : BaseFragment(), StreamingServiceListener,
        CameraVideoCapturer.CameraSwitchHandler {

    companion object {
        val TAG: String = StreamFragment::class.java.name
        private const val CHECK_PERMISSIONS_AND_CONNECT_REQUEST_CODE = 1
        private val NECESSARY_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    }

    private lateinit var binding: FragmentStreamBinding
    private val component by componentProvider { it.plus(StreamModule()) }
    private val viewModel by viewModelProvider { component.viewModel() }


    private val isShowingFrontCamera = AtomicBoolean(true)
    var service: StreamingService? = null
    private lateinit var serviceConnection: ServiceConnection

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stream, container, false)
        binding.viewModel = viewModel
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.stream, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu?.findItem(R.id.menu_toggle_front)?.isVisible = !isShowingFrontCamera.get()
        menu?.findItem(R.id.menu_toggle_rear)?.isVisible = isShowingFrontCamera.get()
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_toggle_front, R.id.menu_toggle_rear -> service?.switchCamera(this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissionsAndConnect()
    }

    private fun checkPermissionsAndConnect() {
        if (context!!.areAllPermissionsGranted(*NECESSARY_PERMISSIONS)) {
            attachService()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), CHECK_PERMISSIONS_AND_CONNECT_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) = when (requestCode) {
        CHECK_PERMISSIONS_AND_CONNECT_REQUEST_CODE -> {
            val grantResult = grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (grantResult) {
                checkPermissionsAndConnect()
            } else {
                showNoPermissionsSnackbar()
            }
        }
        else -> {
            error("Unknown permission request code $requestCode")
        }
    }

    private fun showNoPermissionsSnackbar() {
        view?.let { view ->
            Snackbar.make(view, R.string.msg_permissions, Snackbar.LENGTH_LONG)
                    .setAction(R.string.action_settings) {
                        try {
                            context?.startAppSettings()
                        } catch (e: ActivityNotFoundException) {
                            showSnackbarMessage(R.string.error_permissions_couldnt_start_settings, Snackbar.LENGTH_LONG)
                        }
                    }
                    .show()
        }
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


    override fun onCameraSwitchDone(p0: Boolean) {
        isShowingFrontCamera.set(!isShowingFrontCamera.get())
        activity?.invalidateOptionsMenu()

    }

    override fun onCameraSwitchError(p0: String?) {
        activity?.invalidateOptionsMenu()
    }
}