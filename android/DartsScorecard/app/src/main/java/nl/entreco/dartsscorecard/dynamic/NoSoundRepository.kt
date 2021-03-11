package nl.entreco.dartsscorecard.dynamic

import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import nl.entreco.domain.mastercaller.Sound
import nl.entreco.domain.mastercaller.SoundRepository
import nl.entreco.liblog.Logger

/*************************************************************************
 *
 * ONWARD CONFIDENTIAL
 * __________________
 *
 *  [2021] ONWARD
 *  All Rights Reserved.
 *
 */
class NoSoundRepository(
        private val logger: Logger,
        private val request: SplitInstallRequest,
        private val manager: SplitInstallManager
) : SoundRepository {

    override fun play(sound: Sound) {
        logger.i("Sound play: $sound")
        manager.startInstall(request)
                .addOnCompleteListener{ logger.i("Sound - installed") }
                .addOnSuccessListener { logger.i("Sound - loading") }
                .addOnFailureListener { logger.i("Sound - error: ${it.localizedMessage}") }
    }

    override fun release() {
        logger.i("Sound release")
    }
}