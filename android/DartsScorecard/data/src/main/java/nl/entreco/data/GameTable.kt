package nl.entreco.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Entreco on 12/12/2017.
 */
@Entity(tableName = "Game")
class GameTable {
    @PrimaryKey
    var uid: String = ""

    @ColumnInfo(name = "startScore")
    var startScore: Int = -1

    @ColumnInfo(name = "startIndex")
    var startIndex: Int = 0

    @ColumnInfo(name = "numLegs")
    var numLegs: Int = 0

    @ColumnInfo(name = "numSets")
    var numSets: Int = 0

//    @ColumnInfo(name = "teams")
//    lateinit var teams: Array<String>

}