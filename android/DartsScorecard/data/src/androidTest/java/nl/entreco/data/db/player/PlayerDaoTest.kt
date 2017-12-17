package nl.entreco.data.db.player

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import nl.entreco.data.DscDatabase
import nl.entreco.data.TestProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
    private var givenPlayer: PlayerTable? = null
    private var actualPlayer: PlayerTable? = null
    private var actualRow: Long = -1

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
        givenPlayer("remco", 11)
        whenCreatingPlayer()
        assertEquals(1, actualRow)
    }

    @Test
    fun fetchByName() {
        createPlayer("remco")
        whenFetchingByName("remco")
        assertNotNull(actualPlayer)
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

    private fun createPlayer(name: String, fav: Int = 0) {
        val player = TestProvider.createPlayer(name, fav)
        playerDao.create(player)
    }

    private fun givenPlayer(name: String, fav: Int = 0) {
        givenPlayer = TestProvider.createPlayer(name, fav)
    }

    private fun whenCreatingPlayer() {
        actualRow = playerDao.create(givenPlayer!!)
    }

    private fun whenFetchingByName(name: String) {
        actualPlayer = playerDao.fetchByName(name)
    }
}