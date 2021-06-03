package nl.entreco.data.play.local

import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.turn.LocalTurnRepository
import nl.entreco.data.db.turn.TurnDao
import nl.entreco.data.db.turn.TurnMapper
import nl.entreco.data.db.turn.TurnTable
import nl.entreco.domain.model.Turn
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 24/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class LocalTurnRepositoryTest {

    @Mock private lateinit var mockDb: DscDatabase
    @Mock private lateinit var mockTurnDao: TurnDao
    @Mock private lateinit var mockMapper: TurnMapper
    private lateinit var subject: LocalTurnRepository
    private val gameId: Long = 11
    private val playerId: Long = 1066654
    private lateinit var givenTurn: Turn
    private lateinit var expectedTable: TurnTable

    @Before
    fun setUp() {
        whenever(mockDb.turnDao()).thenReturn(mockTurnDao)
        subject = LocalTurnRepository(mockDb, mockMapper)
    }

    @Test
    fun `store should convert to TurnTable()`() {
        givenTurn(Turn())
        whenStoring()
        thenMapperConvertsFrom()
        andCreateIsCalled()
    }

    @Test
    fun fetchTurnsForGame() {
        whenFetchingTurns()
        thenFetchIsCalledOnDao()
    }

    @Test
    fun `it should undo`() {
        subject.undo(5)
        verify(mockTurnDao).undoLast(5)
    }

    private fun givenTurn(turn: Turn) {
        givenTurn = turn
        expectedTable = TurnMapper().from(gameId, playerId, givenTurn)
    }

    private fun whenStoring() {
        whenever(mockMapper.from(gameId, playerId, givenTurn)).thenReturn(expectedTable)
        subject.store(gameId, playerId, givenTurn)
    }

    private fun whenFetchingTurns() {
        subject.fetchTurnsForGame(gameId)
    }

    private fun thenMapperConvertsFrom() {
        verify(mockMapper).from(gameId, playerId, givenTurn)
    }

    private fun thenFetchIsCalledOnDao() {
        verify(mockTurnDao).fetchAll(gameId)
    }

    private fun andCreateIsCalled() {
        verify(mockTurnDao).create(expectedTable)
    }
}