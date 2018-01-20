package nl.entreco.data.play.local

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.game.LocalGameRepository
import nl.entreco.data.db.game.GameDao
import nl.entreco.data.db.game.GameMapper
import nl.entreco.data.db.game.GameTable
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
        val game = subject.create("1|2", 2, 3, 501, 0)
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

    @Test
    fun `it should fetch games by Uid`() {
        givenExistingGames(1)
        whenFetchingByUid(1)
        thenFetchByUidIsCalledOnDao()
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw IllegalState when game with Uid does not exist`() {
        givenExistingGames(1)
        whenFetchingByUid(2)
    }

    @Test
    fun `it should mark game as finished`() {
        givenExistingGames(1)
        whenFinishingGame(1)
        thenUpdateGamesIsCalledOnDao()
    }

    private fun givenExistingGames(){
        val table = GameTable()
        table.startIndex = 0
        table.startScore = 5
        table.numSets = 5
        table.numLegs = 5
        table.teams = "1,2|3"
        table.id = 1
        val games = listOf(table)
        whenever(mockGameDao.fetchAll()).thenReturn(games)
    }

    private fun givenExistingGames(id: Long){
        val table = GameTable()
        table.startIndex = 0
        table.startScore = 5
        table.numSets = 5
        table.numLegs = 5
        table.teams = "1,2|3"
        table.id = id
        whenever(mockGameDao.fetchBy(id)).thenReturn(table)
    }

    private fun givenNoGames(){
        whenever(mockGameDao.fetchAll()).thenReturn(emptyList())
    }

    private fun whenFetchingAll() {
        subject.fetchLatest()
    }

    private fun whenFetchingByUid(id: Long){
        subject.fetchBy(id)
    }

    private fun whenFinishingGame(gameId: Long) {
        subject.finish(gameId)
    }

    private fun thenFetchAllIsCalledOnDao() {
        verify(mockGameDao).fetchAll()
    }

    private fun thenFetchByUidIsCalledOnDao() {
        verify(mockGameDao).fetchBy(any())
    }

    private fun thenUpdateGamesIsCalledOnDao(){
        verify(mockGameDao).updateGames(isA())
    }
}