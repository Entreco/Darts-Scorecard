package nl.entreco.domain.play.usecase

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.play.model.*
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.repository.GameRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 14/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class CreateGameUsecaseTest {

    @Mock private lateinit var mockGameRepository : GameRepository

    private lateinit var subject : CreateGameUsecase

    private var mockTurnHandler : TurnHandler = TurnHandler(arrayOf(Team(Player("1")), Team(Player("2"))))
    private var arbiter = Arbiter(Score(), mockTurnHandler)
    private var game = Game(arbiter)


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