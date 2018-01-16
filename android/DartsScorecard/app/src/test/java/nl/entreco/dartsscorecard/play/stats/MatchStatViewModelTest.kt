package nl.entreco.dartsscorecard.play.stats

import nl.entreco.domain.model.Score
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.setup.game.CreateGameRequest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 14/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class MatchStatViewModelTest {

    private lateinit var subject: MatchStatViewModel
    private var givenTeams: Array<Team> = emptyArray()
    private var givenScores: Array<Score> = emptyArray()
    private var givenCreateRequest: CreateGameRequest = CreateGameRequest(8,5,3,1)

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
    fun `it should have empty 180s when loaded`() {
        givenTeams("hein", "henk")
        givenSubjectLoaded()
        thenNumberOf180sIs("-", "-")
    }

    private fun givenSubjectLoaded() {
        subject = MatchStatViewModel()
        subject.onLoaded(givenTeams, givenScores, givenCreateRequest, null)
    }

    private fun givenTeams(vararg names: String){
        val teams = mutableListOf<Team>()
        names.forEachIndexed { index, name ->
            teams.add(Team(arrayOf(Player(name, id = index.toLong()))))
        }
        givenTeams = teams.toTypedArray()
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