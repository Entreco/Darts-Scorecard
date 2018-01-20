package nl.entreco.dartsscorecard.play.stats

import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.model.Game
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.start.Play01Response
import nl.entreco.domain.play.stats.FetchGameStatsUsecase
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

    @Mock private lateinit var mockFetchGameStatsUsecase : FetchGameStatsUsecase
    @Mock private lateinit var mockGame : Game
    @Mock private lateinit var mockResponse : Play01Response
    private lateinit var subject: MatchStatViewModel
    private var givenTeams: Array<Team> = emptyArray()
    private var givenScores: Array<Score> = emptyArray()

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
        whenever(mockResponse.game).thenReturn(mockGame)
        whenever(mockResponse.teamIds).thenReturn("1|2")
        subject = MatchStatViewModel(mockFetchGameStatsUsecase)
        subject.onLoaded(givenTeams, givenScores, mockResponse, null)
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