package nl.entreco.data.db.player

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import nl.entreco.data.DscDatabase
import nl.entreco.data.TestProvider
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Entreco on 16/12/2017.
 */
@RunWith(AndroidJUnit4::class)
class PlayerDaoTest {

    private lateinit var playerDao: PlayerDao
    private lateinit var db: DscDatabase

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getContext()
        db = Room.inMemoryDatabaseBuilder(context, DscDatabase::class.java).build()
        playerDao = db.playerDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun create() {
        val player = TestProvider.createPlayer("remco", 20)
        val row = playerDao.create(player)
        assertEquals(1, row)
    }

    @Test
    fun fetchByUid() {
        val player = TestProvider.createPlayer("remco", 20)
        assertNull(playerDao.fetchByUid(player.uid))
        playerDao.create(player)
        assertNotNull(playerDao.fetchByUid(player.uid))
    }

    @Test
    fun fetchByName() {
        val player = TestProvider.createPlayer("remco", 20)
        assertNull(playerDao.fetchByName("remco"))
        playerDao.create(player)
        assertNotNull(playerDao.fetchByName(player.name))
    }

    @Test
    fun fetchAll() {
        createPlayer("remco", 20)
        createPlayer("eva", 10)
        createPlayer("guusje", -1)
        createPlayer("sibbel", 0)

        val players = playerDao.fetchAll()
        assertNotNull(players)

        assertEquals(4, players.size)
        assertEquals("remco", players[0].name)
        assertEquals("eva", players[1].name)
        assertEquals("guusje", players[2].name)
        assertEquals("sibbel", players[3].name)
    }

    private fun createPlayer(name: String, fav: Int) {
        val player = TestProvider.createPlayer(name, fav)
        playerDao.create(player)
    }
}