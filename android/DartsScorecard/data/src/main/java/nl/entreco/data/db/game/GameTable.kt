package nl.entreco.data.db.game

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Entreco on 12/12/2017.
 */
@Entity(tableName = "Game")
class GameTable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "startScore")
    var startScore: Int = -1

    @ColumnInfo(name = "startIndex")
    var startIndex: Int = 0

    @ColumnInfo(name = "numLegs")
    var numLegs: Int = 0

    @ColumnInfo(name = "numSets")
    var numSets: Int = 0

    @ColumnInfo(name = "teams")
    var teams: String = ""

    @ColumnInfo(name = "finished")
    var finished: Boolean = false

    @ColumnInfo(name = "winningTeam")
    var winningTeam: String = ""

}