package nl.entreco.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import nl.entreco.data.db.game.GameDao
import nl.entreco.data.db.game.GameTable
import nl.entreco.data.db.player.PlayerDao
import nl.entreco.data.db.player.PlayerTable

/**
 * Created by Entreco on 12/12/2017.
 */
@Database(entities = [(GameTable::class), (PlayerTable::class)], version = 1, exportSchema = false)
abstract class DscDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun playerDao(): PlayerDao
}