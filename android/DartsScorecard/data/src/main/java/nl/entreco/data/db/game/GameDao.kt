package nl.entreco.data.db.game

import androidx.room.*

/**
 * Created by Entreco on 12/12/2017.
 */
@Dao
interface GameDao {

    @Query("SELECT * FROM Game ORDER BY id DESC")
    fun fetchAll(): List<GameTable>

    @Query("SELECT * FROM Game WHERE id = :id")
    fun fetchBy(id: Long): GameTable?

    @Update
    fun updateGames(vararg games: GameTable)

    @Update
    fun undoFinish(vararg games: GameTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(game: GameTable): Long

    @Delete
    fun delete(vararg games: GameTable)
}