package nl.entreco.data.db.hiscores

import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.player.PlayerDao
import nl.entreco.data.db.profile.ProfileDao
import nl.entreco.domain.hiscores.HiScore
import nl.entreco.domain.repository.HiScoreRepository

class LocalHiScoresRepository(db: DscDatabase, private val mapper: HiScoreMapper) : HiScoreRepository {
    private val playerDao: PlayerDao = db.playerDao()
    private val profileDao: ProfileDao = db.profileDao()

    override fun fetchHiScores(): List<HiScore> {
        return playerDao.fetchAll().map {
            val profile = profileDao.fetchByPlayerId(it.id)
            mapper.to(it, profile)
        }
    }
}