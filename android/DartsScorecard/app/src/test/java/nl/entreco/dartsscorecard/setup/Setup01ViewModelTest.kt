package nl.entreco.dartsscorecard.setup

import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.common.log.Logger
import nl.entreco.domain.launch.ExtractTeamsRequest
import nl.entreco.domain.launch.ExtractTeamsResponse
import nl.entreco.domain.launch.ExtractTeamsUsecase
import nl.entreco.domain.setup.game.CreateGameRequest
import nl.entreco.domain.setup.game.CreateGameResponse
import nl.entreco.domain.setup.game.CreateGameUsecase
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
    @Mock private lateinit var mockLogger: Logger
    @Mock private lateinit var mockNavigator: Setup01Navigator

    private val teamExtractDoneCaptor = argumentCaptor<(ExtractTeamsResponse) -> Unit>()
    private val teamExtractFailCaptor = argumentCaptor<(Throwable) -> Unit>()
    private val gameCreateDoneCaptor = argumentCaptor<(CreateGameResponse) -> Unit>()
    private val gameCreateFailCaptor = argumentCaptor<(Throwable) -> Unit>()

    private lateinit var givenTeamNamesString: String
    private lateinit var givenTeamIdsString: String
    private var givenStartScore: Int = -1
    private var givenStartIndex: Int = -1
    private var givenStartLegs: Int = -1
    private var givenStartSets: Int = -1
    private lateinit var givenCreateGameRequest: CreateGameRequest
    private lateinit var givenCreateGameResponse: CreateGameResponse

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
        givenTeamNamesString = "p1,p2|p3"
        givenTeamIdsString = "1,2|3"
        givenStartScore = 3
        givenStartIndex = 4
        givenStartLegs = 5
        givenStartSets = 6
        givenCreateGameRequest = CreateGameRequest(givenStartScore, givenStartIndex, givenStartLegs, givenStartSets)
        givenCreateGameResponse = CreateGameResponse(88, givenTeamIdsString, givenStartScore, givenStartIndex, givenStartLegs, givenStartSets)
        subject = Setup01ViewModel(mockCreateGameUsecase, mockExtractTeamUsecase, mockLogger)
    }

    private fun givenStartNewGamePressed() {
        subject.onStartPressed(mockNavigator, givenCreateGameRequest, ExtractTeamsRequest(givenTeamNamesString))
    }

    private fun whenTeamExists() {
        verify(mockExtractTeamUsecase).exec(eq(ExtractTeamsRequest(givenTeamNamesString)), teamExtractDoneCaptor.capture(), any())
        teamExtractDoneCaptor.lastValue.invoke(ExtractTeamsResponse(givenTeamIdsString))
    }

    private fun whenTeamDoesNotExist() {
        verify(mockExtractTeamUsecase).exec(eq(ExtractTeamsRequest(givenTeamNamesString)), any(), teamExtractFailCaptor.capture())
        teamExtractFailCaptor.lastValue.invoke(Throwable("Unable to create team $givenTeamNamesString"))
    }

    private fun andGameCreateSucceeds() {
        verify(mockCreateGameUsecase).exec(any(), eq(givenTeamIdsString), gameCreateDoneCaptor.capture(), any())
        gameCreateDoneCaptor.lastValue.invoke(givenCreateGameResponse)
    }

    private fun butGameCreateFails() {
        verify(mockCreateGameUsecase).exec(any(), eq(givenTeamIdsString), any(), gameCreateFailCaptor.capture())
        gameCreateFailCaptor.lastValue.invoke(Throwable("Unable to create game"))
    }

    private fun thenPlay01ActivityIsLaunched() {
        verify(mockNavigator).launch(givenCreateGameResponse)

    }

    private fun thenPlay01ActivityIsNotLaunched() {
        verifyZeroInteractions(mockNavigator)
    }
}