package nl.entreco.dartsscorecard.play.stats

import nl.entreco.domain.model.Turn
import nl.entreco.domain.play.ScoreEstimator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 13/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class TeamStatModelTest {

    private var givenTurns: Array<Turn> = emptyArray()
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

    @Test
    fun `it should populate stats with prefilled turns`() {
        givenTurns(180, 180)
        givenSubject("Pjotr")
        thenStatsAreNotEmpty()
    }

    private fun givenSubject(name: String) {
        subject = TeamStatModel(name, givenTurns)
    }

    private fun givenTurns(vararg scores: Int) {
        val turns = mutableListOf<Turn>()
        scores.forEach { scored ->
            turns.add(ScoreEstimator().guess(scored, true))
        }

        givenTurns = turns.toTypedArray()
    }

    private fun thenStatsAreEmpty() {
        assertEquals(TeamStatModel.empty, subject.avg.get().toString())
        assertEquals(TeamStatModel.empty, subject.n180.get().toString())
        assertEquals(TeamStatModel.empty, subject.n140.get().toString())
        assertEquals(TeamStatModel.empty, subject.n100.get().toString())
    }

    private fun thenStatsAreNotEmpty() {
        assertNotEquals(TeamStatModel.empty, subject.avg.get())
        assertNotEquals(TeamStatModel.empty, subject.n180.get())
        assertNotEquals(TeamStatModel.empty, subject.n140.get())
        assertNotEquals(TeamStatModel.empty, subject.n100.get())
    }

    private fun thenNameIs(expectedName: String) {
        assertEquals(expectedName, subject.name)
    }
}