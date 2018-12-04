package nl.entreco.data.db.meta

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by entreco on 10/01/2018.
 */
@Dao
interface MetaDao {

    @Query("SELECT * FROM TurnMeta WHERE id = :id")
    fun fetchById(id: Long): MetaTable

    @Query("SELECT * FROM TurnMeta WHERE player = :playerId")
    fun fetchByPlayer(playerId: Long): List<MetaTable>

    @Query("SELECT * FROM TurnMeta WHERE game = :gameId")
    fun fetchAll(gameId: Long): List<MetaTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(stat: MetaTable): Long

    @Query("DELETE FROM TurnMeta WHERE game = :gameId AND id = (SELECT MAX(id) FROM TurnMeta)")
    fun undoLast(gameId: Long): Int
}