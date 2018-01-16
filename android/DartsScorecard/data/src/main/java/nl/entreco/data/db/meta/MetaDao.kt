package nl.entreco.data.db.meta

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

/**
 * Created by entreco on 10/01/2018.
 */
@Dao
interface MetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(stat: MetaTable): Long
}