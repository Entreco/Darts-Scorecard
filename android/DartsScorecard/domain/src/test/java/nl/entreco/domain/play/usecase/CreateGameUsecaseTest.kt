package nl.entreco.domain.play.usecase

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.play.TestBackground
import nl.entreco.domain.play.TestForeground
import nl.entreco.domain.play.model.players.TeamsString
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

    private lateinit var subject: CreateGameUsecase

    private var setup = CreateGameInput(501, 0, 3, 2)
    private var teamString = TeamsString("a|b")
    private var mockBg = TestBackground()
    private var mockFg = TestForeground()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `it should create a game and start it`() {
        givenCreateGameUsecase()
        whenStartIsCalled()
        thenGameIsStarted()
    }

    private fun givenCreateGameUsecase() {
        subject = CreateGameUsecase(mockGameRepository, mockBg, mockFg)
    }

    private fun whenStartIsCalled() {
        subject.start(setup, teamString, mockCallback)
        verify(mockGameRepository).create(any(), teamString.asString(), eq(501), eq(0), eq(3), eq(2))
    }

    private fun thenGameIsStarted() {
        verify(mockCallback).onGameCreated(any(), eq(setup))
    }
}