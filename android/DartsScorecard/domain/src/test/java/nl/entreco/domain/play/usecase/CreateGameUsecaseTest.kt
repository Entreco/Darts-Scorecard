package nl.entreco.domain.play.usecase

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.play.TestBackground
import nl.entreco.domain.play.TestForeground
import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.repository.GameRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 12/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class CreateGameUsecaseTest {

    @Mock private lateinit var mockCallback: CreateGameUsecase.Callback
    @Mock private lateinit var mockGameRepository: GameRepository
    @Mock private lateinit var mockArbiter: Arbiter

    private lateinit var subject: CreateGameUsecase

    private var setup = SetupModel(501, 0, 3, 2)
    private lateinit var game: Game
    private var mockBg = TestBackground()
    private var mockFg = TestForeground()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        game = Game(mockArbiter)
    }

    @Test
    fun `it should create a game and start it`() {
        givenCreateGameUsecase()
        whenStartIsCalled()
        thenGameIsStarted()
        andCallbackIsNotified()
    }

    private fun givenCreateGameUsecase() {
        subject = CreateGameUsecase(mockGameRepository, mockBg, mockFg)
    }

    private fun whenStartIsCalled() {
        whenever(mockGameRepository.create(setup)).then { game }
        subject.start(setup, mockCallback)
    }

    private fun thenGameIsStarted() {
        verify(mockGameRepository).create(setup)
    }

    private fun andCallbackIsNotified() {
        verify(mockCallback).onGameCreated(game, setup)
    }

}