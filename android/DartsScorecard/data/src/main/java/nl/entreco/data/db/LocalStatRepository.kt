package nl.entreco.data.db

import nl.entreco.data.db.stat.StatDao
import nl.entreco.data.db.stat.StatTable
import nl.entreco.domain.model.Stats
import nl.entreco.domain.repository.StatRepository

/**
 * Created by entreco on 10/01/2018.
 */
class LocalStatRepository(db: DscDatabase) : StatRepository {
    private val statDao: StatDao = db.statDao()
    override fun create(playerId: Long, turnId: Long, gameId: Long, stat: Stats): Long {
        val table = StatTable()
        table.playerId = playerId
        table.turnId = turnId
        table.gameId = gameId

        // TODO: Store Stats smart
        // CYPHER   , MULTIPLIER, DART_FOR_PLAYER   , DART_IN_LEG   , SCORE_BEFORE  , TARGET, TM    ,
        // 20       , 3x        , 1                 , 1             , 501           , 20    , 3x    ,
        // 20       , 3x        , 1                 , 2             , 501           , 20    , 3x    ,


        // Sample Queries:
        // select (number * multiplier) as scored FROM Stats where Player=2
        // select  turn, (number * multiplier) as scored FROM Stats where Player=3 group by turn

        return statDao.create(table)
    }
}