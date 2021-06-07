package nl.entreco.data.play.local

import org.mockito.kotlin.any
import org.mockito.kotlin.isA
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.game.GameDao
import nl.entreco.data.db.game.GameMapper
import nl.entreco.data.db.game.GameTable
import nl.entreco.data.db.game.LocalGameRepository
import org.junit.Assert.assertEquals
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

    private var actualFinishedGamesCount : Int = 0

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
        whenFinishingGame(1, "1")
        thenUpdateGamesIsCalledOnDao()
    }

    @Test
    fun `it should store winningTeam when finishing`() {
        givenExistingGames(1)
        whenFinishingGame(1, "1,2")
        thenUpdateGamesIsCalledOnDao()
    }

    @Test
    fun `it should unmark game as finished`() {
        givenExistingGames(1)
        whenUnFinishingGame(1)
        thenUndoFinishIsCalledOnDao()
    }

    @Test
    fun `it should return finished game count (0)`() {
        givenExistingGames(1)
        whenCountingFinishedGames()
        thenNumberOfFinishedGamesIs(0)
    }

    @Test
    fun `it should return finished game count (10)`() {
        givenFinishedGames(4)
        whenCountingFinishedGames()
        thenNumberOfFinishedGamesIs(4)
    }

    private fun givenExistingGames() {
        val table = GameTable()
        table.startIndex = 0
        table.startScore = 5
        table.numSets = 5
        table.numLegs = 5
        table.teams = "1,2|3"
        table.finished = false
        table.id = 1
        val games = listOf(table)
        whenever(mockGameDao.fetchAll()).thenReturn(games)
    }

    private fun givenFinishedGames(count: Int){
        val games = mutableListOf<GameTable>()
        (0 until count).forEach {
            val table = GameTable()
            table.startIndex = 0
            table.startScore = 5
            table.numSets = 5
            table.numLegs = 5
            table.teams = "1,2|3"
            table.finished = true
            table.id = it.toLong()
            games.add(table)
        }
        whenever(mockGameDao.fetchAll()).thenReturn(games)
    }

    private fun givenExistingGames(id: Long) {
        val table = GameTable()
        table.startIndex = 0
        table.startScore = 5
        table.numSets = 5
        table.numLegs = 5
        table.teams = "1,2|3"
        table.finished = false
        table.id = id
        whenever(mockGameDao.fetchBy(id)).thenReturn(table)
    }

    private fun givenNoGames() {
        whenever(mockGameDao.fetchAll()).thenReturn(emptyList())
    }

    private fun whenFetchingAll() {
        subject.fetchLatest()
    }

    private fun whenFetchingByUid(id: Long) {
        subject.fetchBy(id)
    }

    private fun whenFinishingGame(gameId: Long, winningTeam: String) {
        subject.finish(gameId, winningTeam)
    }

    private fun whenUnFinishingGame(gameId: Long) {
        subject.undoFinish(gameId)
    }

    private fun whenCountingFinishedGames(){
        actualFinishedGamesCount = subject.countFinishedGames()
    }

    private fun thenFetchAllIsCalledOnDao() {
        verify(mockGameDao).fetchAll()
    }

    private fun thenFetchByUidIsCalledOnDao() {
        verify(mockGameDao).fetchBy(any())
    }

    private fun thenUpdateGamesIsCalledOnDao() {
        verify(mockGameDao).updateGames(isA())
    }

    private fun thenUndoFinishIsCalledOnDao() {
        verify(mockGameDao).undoFinish(isA())
    }

    private fun thenNumberOfFinishedGamesIs(expected: Int) {
        assertEquals(expected, actualFinishedGamesCount)
    }
}