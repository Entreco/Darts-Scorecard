package nl.entreco.dartsscorecard.dynamic

import com.google.android.play.core.internal.r
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import nl.entreco.domain.mastercaller.MusicRepository
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
class NoMusicRepository(
        private val logger: Logger,
        private val request: SplitInstallRequest,
        private val manager: SplitInstallManager
) : MusicRepository {

    override fun play() {
        logger.i("Music play")
        manager.startInstall(request)
                .addOnCompleteListener{ logger.i("Music - installed") }
                .addOnSuccessListener { logger.i("Music - loading") }
                .addOnFailureListener { logger.i("Music - error: ${it.localizedMessage}") }
    }

    override fun pause() {
        logger.i("Music pause")
    }

    override fun resume() {
        logger.i("Music resume")
    }

    override fun stop() {
        logger.i("Music stop")
    }
}