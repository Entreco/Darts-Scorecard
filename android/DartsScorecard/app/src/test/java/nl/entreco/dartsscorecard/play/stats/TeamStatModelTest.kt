package nl.entreco.dartsscorecard.play.stats

import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.model.Stat
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
class TeamStatModelTest {

    @Mock
    private lateinit var mockTeam: Team
    private lateinit var subject: TeamStatModel

    @Test
    fun `empty should be double dashes`() {
        assertEquals("--", TeamStatModel.empty)
    }

    @Test
    fun `it should update stats at the start`() {
        subject = TeamStatModel(mockTeam, mutableListOf(Stat(1,2,3,4,5, 6, 15, 5, 9, listOf(10), listOf(11))))
        thenStatsAre("2.00", "4", "5", "6", "11", "33.33%", "9")
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
        whenUpdating(Stat(1,2,3,4,5, 6, 15, 5, 9, listOf(10), listOf(11)))
        thenStatsAre("2.00", "4", "5", "6", "11", "33.33%", "9")
    }

    @Test
    fun `it should update stats (no average)`() {
        givenSubject("Team name")
        whenUpdating(Stat(1,0,0,4,5, 6, 15, 5, 9, listOf(10), listOf(11)))
        thenStatsAre("--", "4", "5", "6", "11", "33.33%", "9")
    }

    @Test
    fun `it should update stats (no percentage)`() {
        givenSubject("Team name")
        whenUpdating(Stat(1,2,3,4,5, 6, 0, 0, 9, listOf(10), listOf(11)))
        thenStatsAre("2.00", "4", "5", "6", "11", "--", "9")
    }

    @Test
    fun `it should update stats (no high checkout)`() {
        givenSubject("Team name")
        whenUpdating(Stat(1,2,3,4,5, 6, 0, 0, 9, emptyList(), emptyList()))
        thenStatsAre("2.00", "4", "5", "6", "--", "--", "9")
    }

    private fun givenSubject(name: String) {
        whenever(mockTeam.toString()).thenReturn(name)
        subject = TeamStatModel(mockTeam)
    }

    private fun whenUpdating(stat: Stat){
        subject.append(listOf(stat))
    }

    private fun thenStatsAreEmpty() {
        assertEquals(TeamStatModel.empty, subject.avg.get().toString())
        assertEquals(TeamStatModel.empty, subject.n180.get().toString())
        assertEquals(TeamStatModel.empty, subject.n140.get().toString())
        assertEquals(TeamStatModel.empty, subject.n100.get().toString())
    }

    private fun thenNameIs(expectedName: String) {
        assertEquals(expectedName, subject.name.get())
    }

    private fun thenStatsAre(avg: String, n180: String, n140: String, n100: String, highestCheckout: String, checkoutPercentage: String, numberOfBreaks: String) {
        assertEquals(avg, subject.avg.get())
        assertEquals(n180, subject.n180.get())
        assertEquals(n140, subject.n140.get())
        assertEquals(n100, subject.n100.get())
        assertEquals(highestCheckout, subject.hCo.get())
        assertEquals(checkoutPercentage, subject.co.get())
        assertEquals(numberOfBreaks, subject.breaks.get())


    }
}