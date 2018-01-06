package nl.entreco.domain.launch.usecase

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.launch.FetchLatestGameResponse
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.GameRepository
import nl.entreco.domain.repository.TeamIdsString
import org.junit.Test

import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 19/12/2017.
 */
class RetrieveLatestGameUsecaseTest {

    @Mock private lateinit var mockGameRepository : GameRepository
    @Mock private lateinit var mockDone : (FetchLatestGameResponse) -> Unit
    @Mock private lateinit var mockFail : (Throwable) -> Unit

    private val fg = TestForeground()
    private val bg = TestBackground()

    private val gameId : Long = 44
    private val teamids = TeamIdsString("1,2,3,4|5,6,7,8")
    private val createRequest = CreateGameRequest(501, 1,2,3)

    private lateinit var subject : RetrieveLatestGameUsecase
    private lateinit var gavenLatestGame : FetchLatestGameResponse

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
        gavenLatestGame = FetchLatestGameResponse(gameId, teamids, createRequest)
        whenever(mockGameRepository.fetchLatest()).thenReturn(gavenLatestGame)
    }

    private fun givenLatestGameDoesNotExists() {
        gavenLatestGame = FetchLatestGameResponse(gameId, teamids, createRequest)
        whenever(mockGameRepository.fetchLatest()).thenThrow(IllegalStateException("no game available"))
    }

    private fun whenFetchingLatest() {
        subject.exec(mockDone, mockFail)
    }

    private fun thenDoneIsCalled() {
        verify(mockDone).invoke(gavenLatestGame)
    }

    private fun thenErrorIsReported() {
        verify(mockFail).invoke(any())
    }

}