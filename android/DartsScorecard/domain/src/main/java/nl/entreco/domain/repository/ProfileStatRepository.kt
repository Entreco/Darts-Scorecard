package nl.entreco.domain.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.profile.ProfileStat


interface ProfileStatRepository {
    @WorkerThread
    fun fetchForPlayer(playerId: Long): ProfileStat
}