package nl.entreco.domain.mastercaller

import androidx.annotation.WorkerThread
import nl.entreco.domain.mastercaller.Sound

/**
 * Created by entreco on 14/03/2018.
 */
interface SoundRepository {
    @WorkerThread
    fun play(sound: Sound)

    @WorkerThread
    fun release()
}