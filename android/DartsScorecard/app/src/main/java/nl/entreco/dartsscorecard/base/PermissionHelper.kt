package nl.entreco.dartsscorecard.base

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.areAllPermissionsGranted
import nl.entreco.dartsscorecard.di.streaming.StreamScope
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.dartsscorecard.play.stream.StreamFragment
import nl.entreco.dartsscorecard.startAppSettings
import javax.inject.Inject

class PermissionHelper @Inject constructor(
        @ActivityScope private val context: Context,
        @StreamScope private val streamFragment: StreamFragment
) {

    companion object {
        internal const val CHECK_PERMISSIONS_AND_CONNECT_REQUEST_CODE = 1
        private val NECESSARY_PERMISSIONS = arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)
    }

    fun checkStreamingPermissionsAndConnect(done: () -> Unit) {
        if (context.areAllPermissionsGranted(*NECESSARY_PERMISSIONS)) {
            done()
        } else {
            streamFragment.requestPermissions(
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
                    CHECK_PERMISSIONS_AND_CONNECT_REQUEST_CODE)
        }
    }

    fun onStreamingPermissionResult(grantResults: IntArray, done: () -> Unit) {
        val grantResult = grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        if (grantResult) {
            checkStreamingPermissionsAndConnect(done)
        } else {
            showNoPermissionsSnackbar()
        }
    }

    private fun showNoPermissionsSnackbar() {
        streamFragment.view?.let { view ->
            Snackbar.make(view, R.string.msg_permissions, Snackbar.LENGTH_LONG)
                    .setAction(R.string.action_settings) {
                        try {
                            context.startAppSettings()
                        } catch (e: ActivityNotFoundException) {
                            streamFragment.showSnackbarMessage(
                                    R.string.error_permissions_couldnt_start_settings,
                                    Snackbar.LENGTH_LONG)
                        }
                    }
                    .show()
        }
    }
}