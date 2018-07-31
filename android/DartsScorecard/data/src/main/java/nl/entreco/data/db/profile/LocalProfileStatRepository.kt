package nl.entreco.data.db.profile

import android.support.annotation.WorkerThread
import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.profile.ProfileStat
import nl.entreco.domain.repository.ProfileStatRepository


class LocalProfileStatRepository(db: DscDatabase, private val mapper: ProfileStatMapper) : ProfileStatRepository {
    private val profileDao: ProfileDao = db.profileDao()

    @WorkerThread
    override fun fetchForPlayer(playerId: Long): ProfileStat {
        val table = profileDao.fetchByPlayerId(playerId)
        return mapper.to(table)
    }
}