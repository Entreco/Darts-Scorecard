package nl.entreco.data.db.meta

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by entreco on 10/01/2018.
 */
@Entity(tableName = "TurnMeta")
class MetaTable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "player")
    var playerId: Long = -1

    @ColumnInfo(name = "game")
    var gameId: Long = -1

    @ColumnInfo(name = "turn")
    var turnId: Long = -1

    @ColumnInfo(name = "leg")
    var legNumber: Int = -1

    @ColumnInfo(name = "set")
    var setNumber: Int = -1

    @ColumnInfo(name = "turnInLeg")
    var turnInLeg: Int = -1

    @ColumnInfo(name = "score")
    var score: Int = -1

    @ColumnInfo(name = "atco")
    var atCheckout: Int = 0
}