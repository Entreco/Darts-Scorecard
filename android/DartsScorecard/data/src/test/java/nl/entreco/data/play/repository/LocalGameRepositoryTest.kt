package nl.entreco.data.play.repository

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.DscDatabase
import nl.entreco.data.db.game.GameDao
import nl.entreco.data.db.game.GameMapper
import nl.entreco.data.db.game.GameTable
import nl.entreco.domain.play.model.Game
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 15/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class LocalGameRepositoryTest {

    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockGameDao: GameDao
    private lateinit var subject: LocalGameRepository
    private lateinit var mapper: GameMapper

    @Before
    fun setUp() {
        whenever(mockDb.gameDao()).thenReturn(mockGameDao)
        mapper = GameMapper()
        subject = LocalGameRepository(mockDb, mapper)
    }

    @Test
    fun `it should create a new game`() {
        val game = subject.create("uid", "1|2", 2, 3, 501, 0)
        assertNotNull(game)
    }

    @Test
    fun `it should fetch previous games if they exist`() {
        givenExistingGames()
        whenFetchingAll()
        thenFetchAllIsCalledOnDao()
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw exception when no previous games exist`() {
        givenNoGames()
        whenFetchingAll()
        thenFetchAllIsCalledOnDao()
    }

    private fun thenFetchAllIsCalledOnDao() {
        verify(mockGameDao).fetchAll()
    }

    private fun whenFetchingAll() {
        subject.fetchLatest()
    }

    private fun givenExistingGames(){
        val table = GameTable()
        table.startIndex = 0
        table.startScore = 5
        table.numSets = 5
        table.numLegs = 5
        table.teams = "1,2|3"
        table.uid = "some uid"
        val games = listOf(table)
        whenever(mockGameDao.fetchAll()).thenReturn(games)
    }

    private fun givenNoGames(){
        whenever(mockGameDao.fetchAll()).thenReturn(emptyList())
    }
}