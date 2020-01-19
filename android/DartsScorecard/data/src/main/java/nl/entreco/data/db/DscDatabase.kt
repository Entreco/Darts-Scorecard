package nl.entreco.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import nl.entreco.data.db.bot.BotDao
import nl.entreco.data.db.bot.BotTable
import nl.entreco.data.db.game.GameDao
import nl.entreco.data.db.game.GameTable
import nl.entreco.data.db.meta.MetaDao
import nl.entreco.data.db.meta.MetaTable
import nl.entreco.data.db.player.PlayerDao
import nl.entreco.data.db.player.PlayerTable
import nl.entreco.data.db.profile.ProfileDao
import nl.entreco.data.db.profile.ProfileTable
import nl.entreco.data.db.turn.TurnDao
import nl.entreco.data.db.turn.TurnTable

/**
 * Created by Entreco on 12/12/2017.
 */
@Database(version = 3, exportSchema = false, entities = [
    GameTable::class,
    BotTable::class,
    PlayerTable::class,
    TurnTable::class,
    MetaTable::class,
    ProfileTable::class])
abstract class DscDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun playerDao(): PlayerDao
    abstract fun botDao(): BotDao
    abstract fun turnDao(): TurnDao
    abstract fun metaDao(): MetaDao
    abstract fun profileDao(): ProfileDao

    companion object {
        const val name: String = "dsc_database"
    }
}