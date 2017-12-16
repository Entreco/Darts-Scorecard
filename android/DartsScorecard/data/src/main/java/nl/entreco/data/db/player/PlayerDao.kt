package nl.entreco.data.db.player

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 * Created by Entreco on 16/12/2017.
 */
@Dao
interface PlayerDao {
    @Query("SELECT * FROM Player")
    fun fetchAll(): List<PlayerTable>

    @Query("SELECT * FROM Player WHERE uid = :uid")
    fun fetchByUid(uid: String): PlayerTable?

    @Query("SELECT * FROM Player WHERE name = :name")
    fun fetchByName(name: String): PlayerTable?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(player: PlayerTable): Long
}