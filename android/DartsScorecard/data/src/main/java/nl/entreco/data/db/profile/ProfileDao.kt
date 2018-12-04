package nl.entreco.data.db.profile

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ProfileDao {
    @Query("SELECT * FROM Profile WHERE player = :id")
    fun fetchByPlayerId(id: Long): List<ProfileTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(profile: ProfileTable): Long
}