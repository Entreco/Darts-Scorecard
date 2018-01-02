package nl.entreco.data.db.turn

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 * Created by Entreco on 23/12/2017.
 */
@Dao
interface TurnDao {
    @Query("SELECT * FROM Turn WHERE game = :gameId")
    fun fetchAll(gameId: Long): List<TurnTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(turn: TurnTable): Long
}