package nl.entreco.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by Entreco on 12/12/2017.
 */
@Database(entities = [(GameTable::class)], version = 1)
abstract class DscDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}