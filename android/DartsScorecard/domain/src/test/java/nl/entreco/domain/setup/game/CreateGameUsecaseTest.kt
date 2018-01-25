package nl.entreco.domain.setup.game

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.GameRepository
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

    @Mock private lateinit var mockGameRepository: GameRepository
    @Mock private lateinit var mockOk: (CreateGameResponse) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit


    private lateinit var subject: CreateGameUsecase

    private var setup = CreateGameRequest(501, 0, 3, 2)
    private var teamString = "a|b"
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
        subject.exec(setup, teamString, mockOk, mockFail)
        verify(mockGameRepository).create(eq(teamString.toString()), eq(501), eq(0), eq(3), eq(2))
    }

    private fun thenGameIsStarted() {
        verify(mockOk).invoke(any())
    }
}