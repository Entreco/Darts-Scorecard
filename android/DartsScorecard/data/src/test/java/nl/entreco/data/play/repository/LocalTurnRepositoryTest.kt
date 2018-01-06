package nl.entreco.data.play.repository

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.DscDatabase
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
    private val gameId : Long = 11
    private lateinit var givenTurn : Turn
    private lateinit var expectedTable : TurnTable

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
        andMapperConvertsTo()
    }

    private fun givenTurn(turn: Turn) {
        givenTurn = turn
        expectedTable = TurnMapper().from(gameId, givenTurn)
    }

    private fun whenStoring() {
        whenever(mockMapper.from(gameId, givenTurn)).thenReturn(expectedTable)
        subject.store(gameId, givenTurn)
    }
    private fun whenFetchingTurns() {
        subject.fetchTurnsForGame(gameId)
    }

    private fun thenMapperConvertsFrom() {
        verify(mockMapper).from(gameId, givenTurn)
    }

    private fun thenFetchIsCalledOnDao() {
        verify(mockTurnDao).fetchAll(gameId)
    }

    private fun andCreateIsCalled() {
        verify(mockTurnDao).create(expectedTable)
    }

    private fun andMapperConvertsTo() {
        verify(mockMapper).to(emptyList())
    }

}