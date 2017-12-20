package nl.entreco.dartsscorecard.setup

import android.content.Context
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.launch.TeamNamesString
import nl.entreco.domain.launch.usecase.CreateGameUsecase
import nl.entreco.domain.launch.usecase.ExtractTeamsUsecase
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.TeamIdsString
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 20/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class Setup01ViewModelTest {

    @Mock private lateinit var mockExtractTeamUsecase: ExtractTeamsUsecase
    @Mock private lateinit var mockCreateGameUsecase: CreateGameUsecase
    @Mock private lateinit var mockContext: Context

    private val teamExtractDoneCaptor = argumentCaptor<(TeamIdsString) -> Unit>()
    private val teamExtractFailCaptor = argumentCaptor<(Throwable) -> Unit>()
    private val gameCreateDoneCaptor = argumentCaptor<(RetrieveGameRequest) -> Unit>()
    private val gameCreateFailCaptor = argumentCaptor<(Throwable) -> Unit>()

    private lateinit var givenTeamNamesString: TeamNamesString
    private lateinit var givenTeamIdsString: TeamIdsString
    private lateinit var givenCreateRequest: CreateGameRequest
    private lateinit var givenRetrieveGameRequest: RetrieveGameRequest

    private lateinit var subject: Setup01ViewModel

    @Test
    fun `it should start a new game when all goes well`() {
        givenSetupViewModel()
        givenStartNewGamePressed()
        whenTeamExists()
        andGameCreateSucceeds()
        thenPlay01ActivityIsLaunched()
    }

    @Test
    fun `it should not start new game, when team cannot be extracted`() {
        givenSetupViewModel()
        givenStartNewGamePressed()
        whenTeamDoesNotExist()
        thenPlay01ActivityIsNotLaunched()
    }

    @Test
    fun `it should not start new game, when game cannot be created`() {
        givenSetupViewModel()
        givenStartNewGamePressed()
        whenTeamExists()
        butGameCreateFails()
        thenPlay01ActivityIsNotLaunched()
    }

    private fun givenSetupViewModel() {
        givenTeamNamesString = TeamNamesString("p1,p2|p3")
        givenTeamIdsString = TeamIdsString("1,2|3")
        givenCreateRequest = CreateGameRequest(3, 4, 5, 6)
        givenRetrieveGameRequest = RetrieveGameRequest(88, givenTeamIdsString, givenCreateRequest)
        subject = Setup01ViewModel(mockCreateGameUsecase, mockExtractTeamUsecase)
    }

    private fun givenStartNewGamePressed() {
        subject.onStartPressed(mockContext)
    }

    private fun whenTeamExists() {
        verify(mockExtractTeamUsecase).exec(anyBecauseRandom(), teamExtractDoneCaptor.capture(), any())
        teamExtractDoneCaptor.lastValue.invoke(givenTeamIdsString)
    }

    private fun whenTeamDoesNotExist() {
        verify(mockExtractTeamUsecase).exec(anyBecauseRandom(), any(), teamExtractFailCaptor.capture())
        teamExtractFailCaptor.lastValue.invoke(Throwable("Unable to create team $givenTeamNamesString"))
    }

    private fun andGameCreateSucceeds() {
        verify(mockCreateGameUsecase).exec(any(), eq(givenTeamIdsString), gameCreateDoneCaptor.capture(), any())
        gameCreateDoneCaptor.lastValue.invoke(givenRetrieveGameRequest)
    }

    private fun butGameCreateFails() {
        verify(mockCreateGameUsecase).exec(any(), eq(givenTeamIdsString), any(), gameCreateFailCaptor.capture())
        gameCreateFailCaptor.lastValue.invoke(Throwable("Unable to create game"))
    }

    private fun thenPlay01ActivityIsLaunched() {
        // Unable to verify
    }

    private fun thenPlay01ActivityIsNotLaunched() {
        // Unable to verify
    }

    private fun anyBecauseRandom(): TeamNamesString = any()

}