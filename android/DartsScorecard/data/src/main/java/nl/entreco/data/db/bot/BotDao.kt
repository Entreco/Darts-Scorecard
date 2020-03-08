package nl.entreco.data.db.bot

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BotDao {
    @Query("SELECT * FROM Bot")
    fun fetchAll(): List<BotTable>

    @Query("SELECT * FROM Bot WHERE id = :id")
    fun fetchById(id: Long): BotTable?

    @Query("SELECT * FROM Bot WHERE name = :name")
    fun fetchByName(name: String): BotTable?

    @Update
    fun update(table: BotTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(player: BotTable): Long
}