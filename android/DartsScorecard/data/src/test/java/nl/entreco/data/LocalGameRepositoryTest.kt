package nl.entreco.data

import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.TurnHandler
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 15/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class LocalGameRepositoryTest {

    @InjectMocks private lateinit var subject : LocalGameRepository
    private val mockArbiter = Arbiter(Score(), TurnHandler(arrayOf(Team(Player("1")), Team(Player("2")))))

    @Test
    fun `it should create a new game`() {
        val game = subject.new(mockArbiter)
        assertTrue(Game(mockArbiter) == game)
    }
}