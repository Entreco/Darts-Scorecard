package nl.entreco.data.db.profile

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query


@Dao
interface ProfileDao {
    @Query("SELECT * FROM Profile WHERE player = :id")
    fun fetchByPlayerId(id: Long): List<ProfileTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(profile: ProfileTable): Long
}