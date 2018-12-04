package nl.entreco.data.db.player

import androidx.room.*

/**
 * Created by Entreco on 16/12/2017.
 */
@Dao
interface PlayerDao {
    @Query("SELECT * FROM Player")
    fun fetchAll(): List<PlayerTable>

    @Query("SELECT * FROM Player WHERE id = :id")
    fun fetchById(id: Long): PlayerTable?

    @Query("SELECT * FROM Player WHERE id IN(:ids)")
    fun fetchAllById(ids: LongArray): List<PlayerTable>

    @Query("SELECT * FROM Player WHERE name = :name")
    fun fetchByName(name: String): PlayerTable?

    @Update
    fun update(table: PlayerTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(player: PlayerTable): Long

    @Delete
    fun delete(table: PlayerTable)
}
