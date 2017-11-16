package nl.entreco.domain.play.usecase

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.repository.GameRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 14/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class CreateGameUsecaseTest {

    private lateinit var subject : CreateGameUsecase
    private val arbiter = Arbiter(Score(), 2)
    private val game = Game(arbiter)

    @Mock private lateinit var mockGameRepository : GameRepository

    @Test
    fun `it should create a game and start it`() {
        givenCreateGameUsecase()
        whenStartIsCalled()
        thenGameIsStarted()
    }

    private fun givenCreateGameUsecase() {
        whenever(mockGameRepository.new(arbiter)).then{ game }
        subject = CreateGameUsecase(arbiter, mockGameRepository)
    }

    private fun whenStartIsCalled() {
        subject.start()
    }

    private fun thenGameIsStarted() {
        verify(mockGameRepository).new(arbiter)
    }

}