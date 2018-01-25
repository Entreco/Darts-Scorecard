package nl.entreco.domain.play.stats

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.model.Dart
import nl.entreco.domain.model.Turn
import nl.entreco.domain.repository.TurnRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 30/12/2017.
 */
class StoreTurnUsecaseTest {

    @Mock private lateinit var mockTurnRepository: TurnRepository
    @Mock private lateinit var mockBg: Background
    @Mock private lateinit var mockFg: Foreground
    @Mock private lateinit var done: (StoreTurnResponse)->Unit
    @Mock private lateinit var fail: (Throwable)->Unit
    private val bg = TestBackground()
    private val fg = TestForeground()
    private lateinit var subject: StoreTurnUsecase

    private var givenPlayerId : Long = -1
    private var givenGameId : Long = -1
    private lateinit var givenTurn : Turn
    private lateinit var givenStoreRequest : StoreTurnRequest

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `it should store turns using Repository`() {
        givenViewModel(true)
        givenGameIdAndTurn(66,22, Turn())
        whenStoringTurn()
        thenTurnIsStored()
    }


    @Test
    fun `it should store turns even when throwing`() {
        givenViewModel(true)
        givenGameIdAndTurn(44,22, Turn())
        whenStoringTurnThrows(RuntimeException("oops"))
        thenExceptionIsHandled()
    }

    @Test
    fun `it should store turns on background`() {
        givenViewModel(false)
        givenGameIdAndTurn(18, 88, Turn(Dart.SINGLE_1))
        whenStoringTurn()
        thenBackgroundThreadIsUsed()
    }

    private fun givenViewModel(realExecutors: Boolean) {
        subject = if(realExecutors) {
            StoreTurnUsecase(mockTurnRepository, bg, fg)
        } else {
            StoreTurnUsecase(mockTurnRepository, mockBg, mockFg)
        }
    }

    private fun givenGameIdAndTurn(playerId: Long, gameId: Long, turn: Turn) {
        givenPlayerId = playerId
        givenGameId = gameId
        givenTurn = turn
        givenStoreRequest = StoreTurnRequest(givenPlayerId, gameId, turn)
    }

    private fun whenStoringTurn() {
        subject.exec(givenStoreRequest, done, fail)
    }

    private fun whenStoringTurnThrows(err: Throwable) {
        whenever(mockTurnRepository.store(any(), any(), any())).thenThrow(err)
        subject.exec(givenStoreRequest, done, fail)
    }

    private fun thenTurnIsStored() {
        verify(mockTurnRepository).store(givenGameId, givenPlayerId, givenTurn)
    }

    private fun thenExceptionIsHandled(){
        // It's not crashing -> handled
    }

    private fun thenBackgroundThreadIsUsed() {
        verify(mockBg).post(any())
    }

}