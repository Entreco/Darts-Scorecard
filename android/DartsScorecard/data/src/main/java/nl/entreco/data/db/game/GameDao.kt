package nl.entreco.data.db.game

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query


/**
 * Created by Entreco on 12/12/2017.
 */
@Dao
interface GameDao {

    @Query("SELECT * FROM Game")
    fun fetchAll(): List<GameTable>

    @Query("SELECT * FROM Game WHERE uid = :uid")
    fun fetchBy(uid: String): GameTable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(game: GameTable) : Long
}