package nl.entreco.data.db.player

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Entreco on 16/12/2017.
 */
@Entity(tableName = "Player")
class PlayerTable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "fav")
    var fav: String = ""

    @ColumnInfo(name = "image")
    var image: String = ""
}
