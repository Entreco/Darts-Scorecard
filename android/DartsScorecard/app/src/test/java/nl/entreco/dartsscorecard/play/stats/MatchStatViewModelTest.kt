package nl.entreco.dartsscorecard.play.stats

import nl.entreco.domain.model.Dart
import nl.entreco.domain.model.Next
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 14/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class MatchStatViewModelTest {

    @Mock private lateinit var mockNext: Next

    private lateinit var subject: MatchStatViewModel
    private var givenTeams: Array<Team> = emptyArray()
    private var givenScores: Array<Score> = emptyArray()
    private var givenTurns: List<Pair<Long, Turn>> = emptyList()

    @Test
    fun `it should create teamstats when loaded empty`() {
        givenSubjectLoaded()
        thenNumberOfTeamStatsIs(0)
    }

    @Test
    fun `it should create teamstats when loaded 2 teams`() {
        givenTeams("piet", "henk")
        givenSubjectLoaded()
        thenNumberOfTeamStatsIs(2)
    }

    @Test
    fun `it should have empty avg when loaded`() {
        givenTeams("hein", "henk")
        givenSubjectLoaded()
        thenAvageragesAre("-", "-")
    }

    @Test
    fun `it should update avgs when stats change`() {
        givenTeams("", "")
        givenSubjectLoaded()
        whenStatsChangeFor(0, Turn(Dart.SINGLE_1, Dart.SINGLE_1, Dart.SINGLE_1))
        thenAvageragesAre("3.00", "-")
    }

    @Test
    fun `it should have empty 180s when loaded`() {
        givenTeams("hein", "henk")
        givenSubjectLoaded()
        thenNumberOf180sIs("-", "-")
    }

    @Test
    fun `it should update 180s when stats change`() {
        givenTeams("cross", "barney")
        givenSubjectLoaded()
        whenStatsChangeFor(0, Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_20))
        thenNumberOf180sIs("1", "-")
    }

    private fun givenSubjectLoaded() {
        subject = MatchStatViewModel()
        subject.onLoaded(givenTeams, givenScores, givenTurns, null)
    }

    private fun givenTeams(vararg names: String){
        val teams = mutableListOf<Team>()
        names.forEachIndexed { index, name ->
            teams.add(Team(arrayOf(Player(name, id = index.toLong()))))
        }
        givenTeams = teams.toTypedArray()
    }

    private fun whenStatsChangeFor(playerId: Int, turn: Turn) {
        subject.onStatsChange(mockNext, turn, givenTeams[playerId].players[0], givenScores)
    }

    private fun thenNumberOfTeamStatsIs(expected: Int) {
        assertEquals(expected, subject.teamStats.size)
    }

    private fun thenAvageragesAre(vararg avgs: String) {
        avgs.forEachIndexed { index, avg ->
            assertEquals(avg, subject.teamStats[index]?.avg?.get().toString())
        }
    }

    private fun thenNumberOf180sIs(vararg n180s: String) {
        n180s.forEachIndexed { index, n180 ->
            assertEquals(n180, subject.teamStats[index]?.n180?.get().toString())
        }
    }

}