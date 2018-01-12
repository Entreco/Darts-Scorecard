package nl.entreco.data.db.stat

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by entreco on 10/01/2018.
 */
@Entity(tableName = "Stats")
class StatTable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "player")
    var playerId: Long = -1

    @ColumnInfo(name = "game")
    var gameId: Long = -1

    @ColumnInfo(name = "turn")
    var turnId: Long = -1
}