package nl.entreco.data.db.profile

import android.support.annotation.WorkerThread
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.Mapper
import nl.entreco.data.db.player.PlayerDao
import nl.entreco.data.db.player.PlayerTable
import nl.entreco.domain.profile.Profile
import nl.entreco.domain.repository.ProfileInfoRepository

/**
 * Created by entreco on 23/02/2018.
 */
class LocalProfileInfoInfoRepository(db: DscDatabase, private val mapper: Mapper<PlayerTable, Profile>) : ProfileInfoRepository {
    private val playerDao: PlayerDao = db.playerDao()

    @WorkerThread
    override fun fetchAll(players: LongArray): List<Profile> {
        val table = playerDao.fetchAllById(players)
        return mapper.to(table)
    }

    @WorkerThread
    override fun update(id: Long, name: String?, image: String?, double: String?): Profile {
        val player = playerDao.fetchById(id) ?: throw IllegalStateException("Profile does not exist $id")
        val table = mapper.from(id, player, name, image, double)
        playerDao.update(table)
        return mapper.to(table)
    }
}
