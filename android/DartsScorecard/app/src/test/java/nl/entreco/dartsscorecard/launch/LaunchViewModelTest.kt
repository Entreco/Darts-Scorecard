package nl.entreco.dartsscorecard.launch

import android.content.Context
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.launch.FetchLatestGameResponse
import nl.entreco.domain.launch.TeamNamesString
import nl.entreco.domain.launch.usecase.CreateGameUsecase
import nl.entreco.domain.launch.usecase.CreateTeamsUsecase
import nl.entreco.domain.launch.usecase.RetrieveLatestGameUsecase
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.TeamIdsString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 16/12/2017.
 */
class LaunchViewModelTest {

    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockCreateGameUsecase: CreateGameUsecase
    @Mock private lateinit var mockCreatTeamsUsecase: CreateTeamsUsecase
    @Mock private lateinit var mockRetrieveGameUsecase: RetrieveLatestGameUsecase
    @Mock private lateinit var mockOk: (RetrieveGameRequest) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit

    private lateinit var subject: LaunchViewModel

    private lateinit var givenTeamNames: TeamNamesString
    private lateinit var givenRequestCreate: CreateGameRequest

    private val givenGameId = 88L
    private val givenTeamIds = TeamIdsString("1|2")
    private lateinit var expectedGameRequest: RetrieveGameRequest
    private lateinit var expectedFetchResponse: FetchLatestGameResponse

    private val doneTeamIdsCaptor = argumentCaptor<(TeamIdsString) -> Unit>()
    private val doneGameRequestCaptor = argumentCaptor<(RetrieveGameRequest) -> Unit>()
    private val doneLatestRequestCaptor = argumentCaptor<(FetchLatestGameResponse) -> Unit>()
    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = LaunchViewModel(mockCreateGameUsecase, mockCreatTeamsUsecase, mockRetrieveGameUsecase)
    }

    @Test
    fun `it should create Teams, fetch lastest game and notify ok`() {
        givenTeamsAndStartScore("remco|eva", 501)
        whenLastGameIsRetrieved()
        thenDoneIsExecuted()
    }

    @Test
    fun `it should create Teams, and notify failure, when that fails`() {
        givenTeamsAndStartScore("remco|eva", 501)
        whenLastGameIsNotRetrieved()
        thenFailIsExecuted()
    }

    @Test(expected = RuntimeException::class)
    fun `it should create Game, when fetching latest game fails, and notify success`() {
        givenTeamsAndStartScore("remco|eva", 501)
        whenStartingNewGame()
        whenRetrievingTeamsSucceeds()
        butNewGameCreationSucceeds() // TODO: throws RuntimeException, because laucnhing activity is not mocked
    }

    @Test
    fun `it should create Game, when fetching latest game fails, and notify fail when that fails as well`() {
        givenTeamsAndStartScore("remco|eva", 501)
        whenStartingNewGame()
        whenRetrievingTeamsSucceeds()
        andNewGameCreationFails()
        thenFailIsExecuted()
    }

    private fun givenTeamsAndStartScore(teams: String, start: Int) {
        givenTeamNames = TeamNamesString(teams)
        givenRequestCreate = CreateGameRequest(start, 0, 3, 3)
        expectedGameRequest = RetrieveGameRequest(givenGameId, givenTeamIds, givenRequestCreate)
        expectedFetchResponse = FetchLatestGameResponse(givenGameId, givenTeamIds, givenRequestCreate)
    }

    private fun whenStartingNewGame() {
        subject.startNewGame(mockContext)
    }

    private fun whenRetrievingTeamsSucceeds() {
        verify(mockCreatTeamsUsecase).start(any(), doneTeamIdsCaptor.capture(), any())
        doneTeamIdsCaptor.lastValue.invoke(givenTeamIds)
    }

    private fun whenRetrievingTeamsFails() {
        verify(mockCreatTeamsUsecase).start(eq(givenTeamNames), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(Throwable("unable to retrieve teams"))
    }

    private fun whenLastGameIsRetrieved() {
        subject.retrieveLatestGame()
        verify(mockRetrieveGameUsecase).fetchLatest(doneLatestRequestCaptor.capture(), any())
        doneLatestRequestCaptor.lastValue.invoke(expectedFetchResponse)
    }

    private fun whenLastGameIsNotRetrieved() {
        subject.retrieveLatestGame()
        verify(mockRetrieveGameUsecase).fetchLatest(any(), failCaptor.capture())
        failCaptor.lastValue.invoke(Throwable("unable to fetch latest game"))
    }

    private fun butNewGameCreationSucceeds() {
        verify(mockCreateGameUsecase).start(any(), eq(givenTeamIds), doneGameRequestCaptor.capture(), any())
        doneGameRequestCaptor.lastValue.invoke(expectedGameRequest)
    }

    private fun andNewGameCreationFails() {
        verify(mockCreateGameUsecase).start(any(), eq(givenTeamIds), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(Throwable("Unable to create Game"))
    }

    private fun thenDoneIsExecuted() {
        assertEquals(subject.resumeGame.get(), expectedGameRequest)
    }

    private fun thenFailIsExecuted() {
        assertNull(subject.resumeGame.get())
    }
}