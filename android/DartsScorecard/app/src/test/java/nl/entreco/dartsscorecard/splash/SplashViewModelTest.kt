package nl.entreco.dartsscorecard.splash

import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.play.model.players.TeamsString
import nl.entreco.domain.play.usecase.CreateGameInput
import nl.entreco.domain.play.usecase.CreateGameUsecase
import nl.entreco.domain.play.usecase.CreateTeamsUsecase
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 16/12/2017.
 */
class SplashViewModelTest {

    @Mock private lateinit var mockInputs: CreateGameInput
    @Mock private lateinit var mockCallback: CreateGameUsecase.Callback
    @Mock private lateinit var mockCreateGameUsecase: CreateGameUsecase
    @Mock private lateinit var mockCreatTeamsUsecase: CreateTeamsUsecase
    private lateinit var subject: SplashViewModel

    private val teams = TeamsString("1|2")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = SplashViewModel(mockCreateGameUsecase, mockCreatTeamsUsecase)
    }

    @Test
    fun createGameIfNoneExists() {
        subject.createNewGame(mockInputs, teams, mockCallback)
        verify(mockCreateGameUsecase).start(mockInputs, teams, mockCallback)
    }

}