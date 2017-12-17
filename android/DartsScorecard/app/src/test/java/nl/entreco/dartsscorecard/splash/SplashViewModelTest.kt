package nl.entreco.dartsscorecard.splash

import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.play.model.players.TeamIdsString
import nl.entreco.domain.play.model.players.TeamNamesString
import nl.entreco.domain.play.usecase.CreateGameUsecase
import nl.entreco.domain.play.usecase.CreateTeamsUsecase
import nl.entreco.domain.play.usecase.GameSettingsRequest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 16/12/2017.
 */
class SplashViewModelTest {

    @Mock private lateinit var mockInputs: GameSettingsRequest
    @Mock private lateinit var mockCallback: CreateGameUsecase.Callback
    @Mock private lateinit var mockCallback2: CreateTeamsUsecase.Callback
    @Mock private lateinit var mockCreateGameUsecase: CreateGameUsecase
    @Mock private lateinit var mockCreatTeamsUsecase: CreateTeamsUsecase
    private lateinit var subject: SplashViewModel

    private val teamNames = TeamNamesString("remco|eva")
    private val teamIds = TeamIdsString("1|2")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = SplashViewModel(mockCreateGameUsecase, mockCreatTeamsUsecase)
    }

    @Test
    fun createGameIfNoneExists() {
        subject.createNewGame(mockInputs, teamIds, mockCallback)
        verify(mockCreateGameUsecase).start(mockInputs, teamIds, mockCallback)
    }

    @Test
    fun `it should ensure teams exist`() {
        subject.ensureTeamPlayersExist(teamNames, mockCallback2)
        verify(mockCreatTeamsUsecase).start(teamNames, mockCallback2)

    }

    @Test
    fun `it should retrieve last`() {
        subject.retrieveLastGame(mockInputs, teamIds, mockCallback)
        verify(mockCreateGameUsecase).fetchLatest(mockInputs, teamIds, mockCallback)
    }

}