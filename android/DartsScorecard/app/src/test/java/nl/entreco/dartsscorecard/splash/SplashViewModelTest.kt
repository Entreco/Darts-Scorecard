package nl.entreco.dartsscorecard.splash

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.model.players.TeamIdsString
import nl.entreco.domain.splash.TeamNamesString
import nl.entreco.domain.splash.usecase.CreateGameUsecase
import nl.entreco.domain.splash.usecase.CreateTeamsUsecase
import nl.entreco.domain.play.usecase.GameSettingsRequest
import nl.entreco.domain.play.usecase.RetrieveGameRequest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 16/12/2017.
 */
class SplashViewModelTest {

    @Mock private lateinit var mockCreateGameUsecase: CreateGameUsecase
    @Mock private lateinit var mockCreatTeamsUsecase: CreateTeamsUsecase
    @Mock private lateinit var mockOk: (RetrieveGameRequest)->Unit
    @Mock private lateinit var mockFail: (Throwable)->Unit

    private lateinit var subject: SplashViewModel

    private lateinit var givenTeamNames: TeamNamesString
    private lateinit var givenRequest: GameSettingsRequest

    private val givenGameId = 88L
    private val givenTeamIds = TeamIdsString("1|2")
    private lateinit var expectedGameRequest : RetrieveGameRequest

    private val doneTeamIdsCaptor = argumentCaptor<(TeamIdsString)->Unit>()
    private val doneGameRequestCaptor = argumentCaptor<(RetrieveGameRequest)->Unit>()
    private val failCaptor = argumentCaptor<(Throwable)->Unit>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = SplashViewModel(mockCreateGameUsecase, mockCreatTeamsUsecase)
    }

    @Test
    fun `it should create Teams, fetch lastest game and notify ok`() {
        givenTeamsAndStartScore("remco|eva", 501)
        whenStarting()
        whenRetrievingTeamsSucceeds()
        whenLastGameIsRetrieved()
        thenDoneIsExecuted()
    }

    @Test
    fun `it should create Teams, and notify failure, when that fails`() {
        givenTeamsAndStartScore("remco|eva", 501)
        whenStarting()
        whenRetrievingTeamsFails()
        thenFailIsExecuted()
    }

    @Test
    fun `it should create Game, when fetching latest game fails, and notify success`() {
        givenTeamsAndStartScore("remco|eva", 501)
        whenStarting()
        whenRetrievingTeamsSucceeds()
        whenLastGameIsNotRetrieved()
        butNewGameCreationSucceeds()
        thenDoneIsExecuted()
    }

    @Test
    fun `it should create Game, when fetching latest game fails, and notify fail when that fails as well`() {
        givenTeamsAndStartScore("remco|eva", 501)
        whenStarting()
        whenRetrievingTeamsSucceeds()
        whenLastGameIsNotRetrieved()
        andNewGameCreationFails()
        thenFailIsExecuted()
    }

    private fun givenTeamsAndStartScore(teams: String, start: Int){
        givenTeamNames = TeamNamesString(teams)
        givenRequest = GameSettingsRequest(start, 0, 3,3)
        expectedGameRequest = RetrieveGameRequest(givenGameId, givenTeamIds, givenRequest)
    }

    private fun whenStarting(){
        subject.createFrom(givenTeamNames, givenRequest, mockOk, mockFail)
    }

    private fun whenRetrievingTeamsSucceeds() {
        verify(mockCreatTeamsUsecase).start(eq(givenTeamNames), doneTeamIdsCaptor.capture(), any())
        doneTeamIdsCaptor.lastValue.invoke(givenTeamIds)
    }

    private fun whenRetrievingTeamsFails() {
        verify(mockCreatTeamsUsecase).start(eq(givenTeamNames), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(Throwable("unable to retrieve teams"))
    }

    private fun whenLastGameIsRetrieved(){
        verify(mockCreateGameUsecase).fetchLatest(any(), eq(givenTeamIds), doneGameRequestCaptor.capture(), any())
        doneGameRequestCaptor.lastValue.invoke(expectedGameRequest)
    }

    private fun whenLastGameIsNotRetrieved(){
        verify(mockCreateGameUsecase).fetchLatest(any(), eq(givenTeamIds), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(Throwable("unable to fetch latest game"))
    }

    private fun butNewGameCreationSucceeds(){
        verify(mockCreateGameUsecase).start(eq(givenRequest), eq(givenTeamIds), doneGameRequestCaptor.capture(), any())
        doneGameRequestCaptor.lastValue.invoke(expectedGameRequest)
    }

    private fun andNewGameCreationFails(){
        verify(mockCreateGameUsecase).start(eq(givenRequest), eq(givenTeamIds), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(Throwable("Unable to create Game"))
    }

    private fun thenDoneIsExecuted(){
        verify(mockOk).invoke(expectedGameRequest)
    }

    private fun thenFailIsExecuted(){
        verify(mockFail).invoke(any())
    }
}