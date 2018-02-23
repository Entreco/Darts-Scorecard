package nl.entreco.data.db.profile

import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.Mapper
import nl.entreco.data.db.player.PlayerDao
import nl.entreco.data.db.player.PlayerTable
import nl.entreco.domain.profile.Profile
import nl.entreco.domain.repository.ProfileRepository

/**
 * Created by entreco on 23/02/2018.
 */
class LocalProfileRepository(db: DscDatabase, private val mapper: Mapper<PlayerTable, Profile>) : ProfileRepository {

    private val playerDao: PlayerDao = db.playerDao()

    override fun fetchAll(players: LongArray): List<Profile> {
        val table = playerDao.fetchAllById(players)
        return mapper.to(table)
    }

}
