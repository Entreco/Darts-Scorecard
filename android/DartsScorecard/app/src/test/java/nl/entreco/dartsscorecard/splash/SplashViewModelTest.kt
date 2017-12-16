package nl.entreco.dartsscorecard.splash

import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.play.usecase.CreateGameInput
import nl.entreco.domain.play.usecase.CreateGameUsecase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 16/12/2017.
 */
class SplashViewModelTest {

    @Mock private lateinit var mockInputs: CreateGameInput
    @Mock private lateinit var mockCallback: CreateGameUsecase.Callback
    @Mock private lateinit var mockCreateGameUsecase: CreateGameUsecase
    private lateinit var subject : SplashViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = SplashViewModel(mockCreateGameUsecase)
    }

    @Test
    fun createGameIfNoneExists() {
        subject.createGameIfNoneExists(mockInputs, mockCallback)
        verify(mockCreateGameUsecase).start(mockInputs, mockCallback)
    }

}