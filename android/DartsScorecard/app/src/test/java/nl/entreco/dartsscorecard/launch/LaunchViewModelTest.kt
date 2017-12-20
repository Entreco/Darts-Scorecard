package nl.entreco.dartsscorecard.launch

import android.content.Context
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.dartsscorecard.setup.Setup01Activity
import nl.entreco.domain.launch.FetchLatestGameResponse
import nl.entreco.domain.launch.TeamNamesString
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
import org.mockito.exceptions.misusing.NotAMockException

/**
 * Created by Entreco on 16/12/2017.
 */
class LaunchViewModelTest {

    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockRetrieveGameUsecase: RetrieveLatestGameUsecase

    private lateinit var subject: LaunchViewModel

    private lateinit var givenTeamNames: TeamNamesString
    private lateinit var givenRequestCreate: CreateGameRequest

    private val givenGameId = 88L
    private val givenTeamIds = TeamIdsString("1|2")
    private lateinit var expectedGameRequest: RetrieveGameRequest
    private lateinit var expectedFetchResponse: FetchLatestGameResponse

    private val doneLatestRequestCaptor = argumentCaptor<(FetchLatestGameResponse) -> Unit>()
    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = LaunchViewModel(mockRetrieveGameUsecase)
    }

    @Test
    fun `it should store latest game, when it exist`() {
        givenTeamsAndStartScore("remco|eva", 501)
        givenLatestGameExists()
        thenGameIsStoredInObservable()
    }

    @Test
    fun `it should clear latest game, when no latest game`() {
        givenTeamsAndStartScore("remco|eva", 501)
        givenNoLatestGameExists()
        theObservableIsCleared()
    }

    @Test(expected = NotAMockException::class)
    fun `it should launch Setup when 'onNewGame' is pressed`() {
        whenOnNewGameIsClicked()
        thenSetup01IsLaunched()
    }

    @Test(expected = NotAMockException::class)
    fun `it should launch Play01 when 'onResume' is pressed`() {
        whenOnResumeIsClicked()
        thenPlay01IsLaunched()
    }

    private fun givenTeamsAndStartScore(teams: String, start: Int) {
        givenTeamNames = TeamNamesString(teams)
        givenRequestCreate = CreateGameRequest(start, 0, 3, 3)
        expectedGameRequest = RetrieveGameRequest(givenGameId, givenTeamIds, givenRequestCreate)
        expectedFetchResponse = FetchLatestGameResponse(givenGameId, givenTeamIds, givenRequestCreate)

        subject.retrieveLatestGame()
    }

    private fun givenLatestGameExists() {
        verify(mockRetrieveGameUsecase).exec(doneLatestRequestCaptor.capture(), any())
        doneLatestRequestCaptor.lastValue.invoke(expectedFetchResponse)
    }

    private fun givenNoLatestGameExists() {
        verify(mockRetrieveGameUsecase).exec(any(), failCaptor.capture())
        failCaptor.lastValue.invoke(Throwable("unable to fetch latest game"))
    }

    private fun whenOnNewGameIsClicked() {
        subject.onNewGamePressed(mockContext)
    }

    private fun whenOnResumeIsClicked() {
        subject.onResumePressed(mockContext)
    }

    private fun thenGameIsStoredInObservable() {
        assertEquals(subject.resumedGame.get(), expectedGameRequest)
    }

    private fun theObservableIsCleared() {
        assertNull(subject.resumedGame.get())
    }

    private fun thenSetup01IsLaunched(){
        verify(Setup01Activity).launch(mockContext)
    }

    private fun thenPlay01IsLaunched(){
        verify(Play01Activity).startGame(mockContext, expectedGameRequest)
    }
}