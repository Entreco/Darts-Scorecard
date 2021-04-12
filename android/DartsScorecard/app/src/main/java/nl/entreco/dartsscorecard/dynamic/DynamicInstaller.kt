package nl.entreco.dartsscorecard.dynamic

import com.google.android.play.core.ktx.status
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

class DynamicInstaller(
        private val request: SplitInstallRequest,
        private val manager: SplitInstallManager,
) : Installer {

    private var mySessionId = 0

    override fun install(callback: SoundInstalledCallback) {
        val installListener = InstallListener(callback)
        manager.registerListener(installListener)
        manager.startInstall(request)
                .addOnCompleteListener {  }
                .addOnSuccessListener { mySessionId = it }
                .addOnFailureListener { callback.onError() }
    }
}

class InstallListener(
        private val callback: SoundInstalledCallback,
) : SplitInstallStateUpdatedListener {

    override fun onStateUpdate(state: SplitInstallSessionState) {
        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                val totalBytes = state.totalBytesToDownload()
                val bytes = state.bytesDownloaded()
                callback.onProgress(bytes, totalBytes)
            }
            SplitInstallSessionStatus.INSTALLED -> {

                // After a module is installed, you can start accessing its content or
                // fire an intent to start an activity in the installed module.
                // For other use cases, see access code and resources from installed modules.

                // If the request is an on demand module for an Android Instant App
                // running on Android 8.0 (API level 26) or higher, you need to
                // update the app context using the SplitInstallHelper API.
                callback.onComplete()
            }
            SplitInstallSessionStatus.CANCELED,
            SplitInstallSessionStatus.CANCELING,
            SplitInstallSessionStatus.DOWNLOADED,
            SplitInstallSessionStatus.FAILED,
            SplitInstallSessionStatus.INSTALLING,
            SplitInstallSessionStatus.PENDING,
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION,
            SplitInstallSessionStatus.UNKNOWN -> { /* Ignore */ }
        }
    }
}
