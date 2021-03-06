package nl.entreco.domain.play.start

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.model.Turn
import nl.entreco.domain.repository.TurnRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 30/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class RetrieveTurnsUsecaseTest {

    @Mock private lateinit var mockTurnRepository: TurnRepository
    private val mockBg = TestBackground()
    private val mockFg = TestForeground()
    private lateinit var subject: RetrieveTurnsUsecase

    private var givenGameId: Long = -1
    private var givenTurns: List<Pair<Long, Turn>> = emptyList()
    @Mock private lateinit var mockOk: (RetrieveTurnsResponse) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit

    @Test
    fun `it should retrieve turns from database`() {
        givenUsecase(66)
        givenStoredTurns(listOf(Pair(1L, Turn()), Pair(2L, Turn())))
        whenRetrievingTurnsSucceeds()
        thenOkIsExecuted()
    }

    @Test
    fun `it should report error when failing to retrieve turns from database`() {
        givenUsecase(66)
        givenStoredTurns(listOf(Pair(1L, Turn()), Pair(2L, Turn())))
        whenRetrievingTurnsFails(RuntimeException("Something wrong with db"))
        thenFailIsExecuted()
    }

    private fun givenUsecase(gameId: Long) {
        givenGameId = gameId
        subject = RetrieveTurnsUsecase(mockTurnRepository, mockBg, mockFg)
    }

    private fun givenStoredTurns(turns: List<Pair<Long, Turn>>) {
        givenTurns = turns
    }

    private fun whenRetrievingTurnsSucceeds() {
        whenever(mockTurnRepository.fetchTurnsForGame(givenGameId)).thenReturn(givenTurns)
        subject.exec(RetrieveTurnsRequest(givenGameId), mockOk, mockFail)
    }

    private fun whenRetrievingTurnsFails(err: Throwable) {
        whenever(mockTurnRepository.fetchTurnsForGame(givenGameId)).thenThrow(err)
        subject.exec(RetrieveTurnsRequest(givenGameId), mockOk, mockFail)
    }

    private fun thenOkIsExecuted() {
        verify(mockOk).invoke(RetrieveTurnsResponse(givenTurns))
    }

    private fun thenFailIsExecuted() {
        verify(mockFail).invoke(any())
    }
}