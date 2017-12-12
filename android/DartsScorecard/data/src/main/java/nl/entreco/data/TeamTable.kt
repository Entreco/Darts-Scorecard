package nl.entreco.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Entreco on 12/12/2017.
 */
@Entity(tableName = "team")
class TeamTable {
    @PrimaryKey
    lateinit var uid: String

    @ColumnInfo(name = "name")
    lateinit var name : String

    @ColumnInfo(name = "fav")
    lateinit var fav : String
}