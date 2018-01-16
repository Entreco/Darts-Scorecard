package nl.entreco.data.db.turn

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Entreco on 23/12/2017.
 */
@Entity(tableName = "Turn")
class TurnTable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "d1")
    var d1: Int = -1
    @ColumnInfo(name = "d2")
    var d2: Int = -1
    @ColumnInfo(name = "d3")
    var d3: Int = -1
    @ColumnInfo(name = "m1")
    var m1: Int = -1
    @ColumnInfo(name = "m2")
    var m2: Int = -1
    @ColumnInfo(name = "m3")
    var m3: Int = -1
    @ColumnInfo(name = "numDarts")
    var numDarts: Int = -1
    @ColumnInfo(name = "game")
    var game: Long = -1
    @ColumnInfo(name = "player")
    var player: Long = -1
}