package nl.entreco.dartsscorecard.play.stats

import com.nhaarman.mockito_kotlin.whenever
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
    fun `it should have name as specified`() {
        givenSubject("Pjotr")
        thenNameIs("Pjotr")
    }

    @Test
    fun `it should have empty stats initially`() {
        givenSubject("Pjotr")
        thenStatsAreEmpty()
    }

    private fun givenSubject(name: String) {
        whenever(mockTeam.toString()).thenReturn(name)
        subject = TeamStatModel(mockTeam)
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
}