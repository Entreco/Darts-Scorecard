package nl.entreco.data.play.repository

import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.DscDatabase
import nl.entreco.data.db.player.PlayerDao
import nl.entreco.data.db.player.PlayerMapper
import nl.entreco.data.db.player.PlayerTable
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
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
        assertNull(subject.fetchByName("non-existing"))
    }

    @Test
    fun `it should fetch by name`() {
        givenPlayerTable("piet", 12)
        assertNotNull(subject.fetchByName("piet"))
    }

    private fun givenPlayerTable(name: String, double: Int) {
        val table = PlayerTable()
        table.uid = "random uid"
        table.name = name
        table.fav = double.toString()
        whenever(mockPlayerDao.fetchByName(name)).thenReturn(table)
    }
}