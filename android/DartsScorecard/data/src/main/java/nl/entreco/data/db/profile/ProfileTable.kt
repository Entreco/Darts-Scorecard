package nl.entreco.data.db.profile

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Profile")
class ProfileTable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "player")
    var playerId: Long = -1

    @ColumnInfo(name = "game")
    var gameId: Long = -1

    @ColumnInfo(name = "numDarts")
    var numDarts: Int = 0

    @ColumnInfo(name = "numDarts9")
    var numDarts9: Int = 0

    @ColumnInfo(name = "totalScore")
    var totalScore: Int = 0

    @ColumnInfo(name = "totalScore9")
    var totalScore9: Int = 0

    @ColumnInfo(name = "num180s")
    var num180s: Int = 0

    @ColumnInfo(name = "didWin")
    var didWin: Boolean = false
}