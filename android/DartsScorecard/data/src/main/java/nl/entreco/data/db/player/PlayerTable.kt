package nl.entreco.data.db.player

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Entreco on 16/12/2017.
 */
@Entity(tableName = "player")
class PlayerTable {
    @PrimaryKey
    lateinit var uid: String

    @ColumnInfo(name = "name")
    lateinit var name: String

    @ColumnInfo(name = "fav")
    lateinit var fav: String
}