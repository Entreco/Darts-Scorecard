package nl.entreco.domain.repository

import androidx.annotation.WorkerThread
import nl.entreco.domain.profile.Profile

/**
 * Created by entreco on 23/02/2018.
 */
interface ProfileInfoRepository {

    @WorkerThread
    fun fetchAll(players: LongArray): List<Profile>

    @WorkerThread
    fun update(id: Long, name: String?, image: String?, double: String?): Profile
}
