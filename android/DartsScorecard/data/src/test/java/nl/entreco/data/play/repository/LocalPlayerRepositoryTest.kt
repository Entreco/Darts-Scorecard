package nl.entreco.data.play.repository

import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.DscDatabase
import nl.entreco.data.db.player.PlayerDao
import nl.entreco.data.db.player.PlayerMapper
import nl.entreco.data.db.player.PlayerTable
import nl.entreco.domain.play.model.players.Player
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

    @Before
    fun setUp() {
        whenever(mockDb.playerDao()).thenReturn(mockPlayerDao)
        mapper = PlayerMapper()
        subject = LocalPlayerRepository(mockDb, mapper)
    }

    @Test
    fun `it should create a new player`() {
        val player = subject.create("pietje puk", 12)
        assertNotNull(player)
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
        givenPlayerWith("some uid")
        whenFetching("some uid")
        thenPlayerIsNotNull()
    }

    @Test
    fun `it should return null fetching non-existing uid`() {
        givenPlayerWith("some uid")
        whenFetching("another uid")
        thenPlayerIsNull()
    }

    @Test
    fun `it should fecth all existing players`() {
        givenPlayerWith("id1", "id2", "id3")
        whenFetchingAll()
        thenPlayersAreRetrieved(3)
    }

    private fun givenPlayerWith(name: String, double: Int) {
        val table = PlayerTable()
        table.uid = "random uid"
        table.name = name
        table.fav = double.toString()
        whenever(mockPlayerDao.fetchByName(name)).thenReturn(table)
    }

    private fun givenPlayerWith(vararg uid: String) {
        val tables = ArrayList<PlayerTable>()
        uid.forEach {
            val table = givenPlayerWith(it)
            tables.add(table)
        }
        whenever(mockPlayerDao.fetchAll()).thenReturn(tables.toList())
    }

    private fun givenPlayerWith(uid: String) : PlayerTable {
        val table = PlayerTable()
        table.uid = uid
        table.name = "some name"
        table.fav = "1"
        whenever(mockPlayerDao.fetchByUid(uid)).thenReturn(table)
        return table
    }

    private fun whenFetchingByName(name: String) {
        expectedPlayer = subject.fetchByName(name)
    }

    private fun whenFetching(uid: String) {
        expectedPlayer = subject.fetchByUid(uid)
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