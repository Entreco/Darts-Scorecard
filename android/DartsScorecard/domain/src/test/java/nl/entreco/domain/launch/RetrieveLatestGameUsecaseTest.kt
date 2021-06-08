package nl.entreco.domain.launch

import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.libcore.threading.TestBackground
import nl.entreco.libcore.threading.TestForeground
import nl.entreco.domain.repository.GameRepository
import nl.entreco.domain.setup.game.CreateGameRequest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 19/12/2017.
 */
class RetrieveLatestGameUsecaseTest {

    @Mock private lateinit var mockGameRepository: GameRepository
    @Mock private lateinit var mockDone: (FetchLatestGameResponse) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit

    private val fg = TestForeground()
    private val bg = TestBackground()

    private val gameId: Long = 44
    private val teamids = "1,2,3,4|5,6,7,8"
    private val createRequest = CreateGameRequest(501, 1, 2, 3)

    private lateinit var subject: RetrieveLatestGameUsecase
    private lateinit var givenLatestGame: FetchLatestGameResponse

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = RetrieveLatestGameUsecase(mockGameRepository, bg, fg)
    }

    @Test
    fun itShouldReturnLatestGameIfOneExists() {
        givenLatestGameExists()
        whenFetchingLatest()
        thenDoneIsCalled()
    }


    @Test
    fun itShouldReportErrorIfNoLatestGameExists() {
        givenLatestGameDoesNotExists()
        whenFetchingLatest()
        thenErrorIsReported()
    }

    private fun givenLatestGameExists() {
        givenLatestGame = FetchLatestGameResponse(gameId, teamids, createRequest.startScore, createRequest.startIndex, createRequest.numLegs, createRequest.numSets)
        whenever(mockGameRepository.fetchLatest()).thenReturn(givenLatestGame)
    }

    private fun givenLatestGameDoesNotExists() {
        givenLatestGame = FetchLatestGameResponse(gameId, teamids, createRequest.startScore, createRequest.startIndex, createRequest.numLegs, createRequest.numSets)
        whenever(mockGameRepository.fetchLatest()).thenThrow(IllegalStateException("no game available"))
    }

    private fun whenFetchingLatest() {
        subject.exec(mockDone, mockFail)
    }

    private fun thenDoneIsCalled() {
        verify(mockDone).invoke(givenLatestGame)
    }

    private fun thenErrorIsReported() {
        verify(mockFail).invoke(any())
    }

}