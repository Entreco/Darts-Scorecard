package nl.entreco.domain.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.profile.Profile

/**
 * Created by entreco on 23/02/2018.
 */
interface ProfileRepository {

    @WorkerThread
    fun fetchAll(players: LongArray): List<Profile>
}
