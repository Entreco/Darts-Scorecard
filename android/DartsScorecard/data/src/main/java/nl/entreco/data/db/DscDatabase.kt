package nl.entreco.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import nl.entreco.data.db.game.GameDao
import nl.entreco.data.db.game.GameTable
import nl.entreco.data.db.player.PlayerDao
import nl.entreco.data.db.player.PlayerTable
import nl.entreco.data.db.stat.StatDao
import nl.entreco.data.db.stat.StatTable
import nl.entreco.data.db.turn.TurnDao
import nl.entreco.data.db.turn.TurnTable

/**
 * Created by Entreco on 12/12/2017.
 */
@Database(entities = [(GameTable::class), (PlayerTable::class), (TurnTable::class), (StatTable::class)], version = 1, exportSchema = false)
abstract class DscDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun playerDao(): PlayerDao
    abstract fun turnDao(): TurnDao
    abstract fun statDao(): StatDao

    companion object {
        const val name: String = "dsc_database"
    }
}