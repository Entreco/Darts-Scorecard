package nl.entreco.data.db.game

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import nl.entreco.data.DscDatabase
import nl.entreco.data.TestProvider
import nl.entreco.data.db.player.PlayerDao
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by Entreco on 16/12/2017.
 */
class GameDaoTest {
    private lateinit var gameDao: GameDao
    private lateinit var db: DscDatabase

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getContext()
        db = Room.inMemoryDatabaseBuilder(context, DscDatabase::class.java).build()
        gameDao = db.gameDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun create() {
        val game = TestProvider.createGame("remco", 201, 0, 2,2)
        val row = gameDao.create(game)
        Assert.assertEquals(1, row)
    }

}