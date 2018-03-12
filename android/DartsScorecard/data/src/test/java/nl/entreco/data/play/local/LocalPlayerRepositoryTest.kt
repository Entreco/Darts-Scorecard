package nl.entreco.data.play.local

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.player.LocalPlayerRepository
import nl.entreco.data.db.player.PlayerDao
import nl.entreco.data.db.player.PlayerMapper
import nl.entreco.data.db.player.PlayerTable
import nl.entreco.domain.model.players.Player
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 16/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class LocalPlayerRepositoryTest {
    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockPlayerDao: PlayerDao
    private lateinit var mapper: PlayerMapper
    private lateinit var subject: LocalPlayerRepository
    private var expectedPlayer: Player? = null
    private var expectedPlayers: List<Player>? = null
    private val playerCaptor = argumentCaptor<PlayerTable>()

    @Before
    fun setUp() {
        whenever(mockDb.playerDao()).thenReturn(mockPlayerDao)
        mapper = PlayerMapper()
        subject = LocalPlayerRepository(mockDb, mapper)
    }

    @Test
    fun `it should create a new player with non-null id`() {
        val playerId = subject.create("pietje puk", 12)
        assertNotNull(playerId)
    }

    @Test
    fun `it should create a new player with id==0`() {
        val playerId = subject.create("rembrand remser", -112)
        assertTrue(playerId >= 0)
    }

    @Test
    fun `it should create a new player with lowercase name`() {
        subject.create("Pietje Puk", 12)
        verify(mockPlayerDao).create(playerCaptor.capture())
        assertEquals("pietje puk", playerCaptor.lastValue.name)
    }

    @Test
    fun `it should fetch by name (non-existing)`() {
        whenFetchingByName("non-existing")
        thenPlayerIsNull()
    }

    @Test
    fun `it should fetch by name`() {
        givenPlayerWith("piet", 12)
        whenFetchingByName("piet")
        thenPlayerIsNotNull()
    }

    @Test
    fun `it should fetch by uid`() {
        givenPlayerWith(1)
        whenFetching(1)
        thenPlayerIsNotNull()
    }

    @Test
    fun `it should return null fetching non-existing uid`() {
        whenFetching(1)
        thenPlayerIsNull()
    }

    @Test
    fun `it should fetch all existing players`() {
        givenPlayerWith(1, 2, 3)
        whenFetchingAll()
        thenPlayersAreRetrieved(3)
    }

    private fun givenPlayerWith(name: String, double: Int) {
        val table = PlayerTable()
        table.name = name
        table.fav = double.toString()
        whenever(mockPlayerDao.fetchByName(name)).thenReturn(table)
    }

    private fun givenPlayerWith(vararg id: Long) {
        val tables = ArrayList<PlayerTable>()
        id.forEach {
            val table = givenPlayerWith(it)
            tables.add(table)
        }
        whenever(mockPlayerDao.fetchAll()).thenReturn(tables.toList())
    }

    private fun givenPlayerWith(id: Long): PlayerTable {
        val table = PlayerTable()
        table.name = "some name"
        table.fav = "1"
        whenever(mockPlayerDao.fetchById(id)).thenReturn(table)
        return table
    }

    private fun whenFetchingByName(name: String) {
        expectedPlayer = subject.fetchByName(name)
    }

    private fun whenFetching(id: Long) {
        expectedPlayer = subject.fetchById(id)
    }

    private fun whenFetchingAll() {
        expectedPlayers = subject.fetchAll()
    }

    private fun thenPlayerIsNotNull() {
        assertNotNull(expectedPlayer)
    }

    private fun thenPlayerIsNull() {
        assertNull(expectedPlayer)
    }

    private fun thenPlayersAreRetrieved(numPlayers: Int) {
        assertNotNull(expectedPlayers)
        assertEquals(numPlayers, expectedPlayers?.size)
    }
}