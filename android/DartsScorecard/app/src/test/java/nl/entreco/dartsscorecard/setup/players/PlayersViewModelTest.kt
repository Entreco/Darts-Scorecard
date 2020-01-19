package nl.entreco.dartsscorecard.setup.players

import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 31/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class PlayersViewModelTest {

    @Mock private lateinit var mockAdapter: PlayerAdapter
    private lateinit var subject: PlayersViewModel

    @Test
    fun `it should create teamNames for 1 player(0)`() {
        givenSubject()
        givenPlayersMap(TeamPlayer("co", 0))
        thenTeamsAsStringIs("co")
    }

    @Test
    fun `it should create teamNames for 1 player(-1)`() {
        givenSubject()
        givenPlayersMap(TeamPlayer("co", -1))
        thenTeamsAsStringIs("co")
    }

    @Test
    fun `it should create teamNames for 2 players(-1, 0)`() {
        givenSubject()
        givenPlayersMap(TeamPlayer("co", -1), TeamPlayer("henk", 0))
        thenTeamsAsStringIs("co|henk")
    }

    @Test
    fun `it should create teamNames for 3 players(-1, 0, 1)`() {
        givenSubject()
        givenPlayersMap(TeamPlayer("co", 2), TeamPlayer("henk", -1), TeamPlayer("frenk", 0))
        thenTeamsAsStringIs("henk|frenk|co")
    }

    @Test
    fun `it should create teamNames for 3 players in the same team(-1)`() {
        givenSubject()
        givenPlayersMap(TeamPlayer("co", -1), TeamPlayer("henk", -1), TeamPlayer("frenk", -1))
        thenTeamsAsStringIs("co,henk,frenk")
    }

    @Test
    fun `it should create teamNames for 4 players in the different teams(8, 2)`() {
        givenSubject()
        givenPlayersMap(TeamPlayer("co", 8), TeamPlayer("henk", 8), TeamPlayer("frenk", 2), TeamPlayer("a", 2))
        thenTeamsAsStringIs("frenk,a|co,henk")
    }

    @Test
    fun `it should create teamNames for 7 players in the different teams`() {
        givenSubject()
        givenPlayersMap(
                TeamPlayer("a", 6),
                TeamPlayer("b", 0),
                TeamPlayer("c", 0),
                TeamPlayer("d", 4),
                TeamPlayer("e", 2),
                TeamPlayer("f", 2),
                TeamPlayer("g", 2))
        thenTeamsAsStringIs("b,c|e,f,g|d|a")
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw IllegalState when no players available`() {
        givenSubject()
        givenPlayersMap()
        thenTeamsAsStringIs("")
    }

    private fun givenSubject() {
        subject = PlayersViewModel(mockAdapter)
    }

    private fun givenPlayersMap(vararg players: TeamPlayer) {
        if (players.isEmpty()) {
            whenever(mockAdapter.participants()).thenReturn(emptyList())
        } else {
            whenever(mockAdapter.participants()).thenReturn(createMap(*players))
        }
    }

    private fun thenTeamsAsStringIs(expected: String) {
        val teamsRequest = subject.setupTeams()
        teamsRequest.validate()
        assertEquals(expected, teamsRequest.toString())
    }

    private data class TeamPlayer(val name: String, val index: Int)

    private fun createMap(vararg players: TeamPlayer): List<PlayerViewModel> {
        return players.toList().map {
            PlayerViewModel(-1, it.index, it.name)
        }
    }
}