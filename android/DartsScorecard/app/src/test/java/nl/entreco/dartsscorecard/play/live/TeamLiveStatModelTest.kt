package nl.entreco.dartsscorecard.play.live

import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.model.LiveStat
import nl.entreco.domain.model.players.Team
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 13/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class TeamLiveStatModelTest {

    @Mock
    private lateinit var mockTeam: Team
    private lateinit var subject: TeamLiveStatModel

    @Test
    fun `empty should be double dashes`() {
        assertEquals("--", TeamLiveStatModel.empty)
    }

    @Test
    fun `it should update stats at the start`() {
        subject = TeamLiveStatModel(mockTeam, mutableListOf(LiveStat(1, 2, 3, 4, 5, 6, 2, 3, 15, 5, 9, listOf(10), listOf(11))))
        thenStatsAre("2.00", "4", "5", "6", "2", "3", "11", "5/15", "9")
    }

    @Test
    fun `it should have name as specified`() {
        givenSubject("Pjotr")
        thenNameIs("Pjotr")
    }

    @Test
    fun `it should have empty stats initially`() {
        givenSubject("Pjotr")
        thenStatsAreEmpty()
    }

    @Test
    fun `it should update stats (normal case)`() {
        givenSubject("Team name")
        whenUpdating(LiveStat(1, 2, 3, 4, 5, 6, 2, 3, 15, 5, 9, listOf(10), listOf(11)))
        thenStatsAre("2.00", "4", "5", "6", "2", "3", "11", "5/15", "9")
    }

    @Test
    fun `it should update stats (no average)`() {
        givenSubject("Team name")
        whenUpdating(LiveStat(1, 0, 0, 4, 5, 6, 2, 3, 15, 5, 9, listOf(10), listOf(11)))
        thenStatsAre("--", "4", "5", "6", "2", "3", "11", "5/15", "9")
    }

    @Test
    fun `it should update stats (no percentage)`() {
        givenSubject("Team name")
        whenUpdating(LiveStat(1, 2, 3, 4, 5, 6, 2, 3, 0, 0, 9, listOf(10), listOf(11)))
        thenStatsAre("2.00", "4", "5", "6", "2", "3", "11", "--", "9")
    }

    @Test
    fun `it should update stats (no high checkout)`() {
        givenSubject("Team name")
        whenUpdating(LiveStat(1, 2, 3, 4, 5, 6, 2, 3, 0, 0, 9, emptyList(), emptyList()))
        thenStatsAre("2.00", "4", "5", "6", "2", "3", "--", "--", "9")
    }

    private fun givenSubject(name: String) {
        whenever(mockTeam.toString()).thenReturn(name)
        subject = TeamLiveStatModel(mockTeam)
    }

    private fun whenUpdating(liveStat: LiveStat) {
        subject.append(listOf(liveStat))
    }

    private fun thenStatsAreEmpty() {
        assertEquals(TeamLiveStatModel.empty, subject.avg.get().toString())
        assertEquals(TeamLiveStatModel.empty, subject.n180.get().toString())
        assertEquals(TeamLiveStatModel.empty, subject.n140.get().toString())
        assertEquals(TeamLiveStatModel.empty, subject.n100.get().toString())
        assertEquals(TeamLiveStatModel.empty, subject.n60.get().toString())
        assertEquals(TeamLiveStatModel.empty, subject.n20.get().toString())
    }

    private fun thenNameIs(expectedName: String) {
        assertEquals(expectedName, subject.name.get())
    }

    private fun thenStatsAre(avg: String, n180: String, n140: String, n100: String, n60: String, n20: String, highestCheckout: String, checkoutPercentage: String, numberOfBreaks: String) {
        assertEquals(avg, subject.avg.get())
        assertEquals(n180, subject.n180.get())
        assertEquals(n140, subject.n140.get())
        assertEquals(n100, subject.n100.get())
        assertEquals(n60, subject.n60.get())
        assertEquals(n20, subject.n20.get())
        assertEquals(highestCheckout, subject.hCo.get())
        assertEquals(checkoutPercentage, subject.co.get())
        assertEquals(numberOfBreaks, subject.breaks.get())
    }
}