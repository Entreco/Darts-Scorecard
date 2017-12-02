package nl.entreco.domain.play.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Entreco on 02/12/2017.
 */
class ScoreEstimatorTest{
    private val subject : ScoreEstimator = ScoreEstimator()

    @Test
    fun `it should guess all 3 darters correct`() {
        for(scored in 0..181){
            verifyThreeDarts(scored, subject.guess(scored, false))
        }
    }

    @Test
    fun `it should guess all singles correct`() {
        for(scored in 0..181){
            verifySingleDart(scored, subject.guess(scored, true))
        }
    }

    private fun verifySingleDart(scored: Int, actual: Turn) {
        when {
            scored >= 61 -> verifyImpossible(actual)
            scored == 59 -> verifyImpossible(actual)
            scored == 58 -> verifyImpossible(actual)
            scored == 56 -> verifyImpossible(actual)
            scored == 55 -> verifyImpossible(actual)
            scored == 53 -> verifyImpossible(actual)
            scored == 52 -> verifyImpossible(actual)
            scored == 49 -> verifyImpossible(actual)
            scored == 47 -> verifyImpossible(actual)
            scored == 46 -> verifyImpossible(actual)
            scored == 44 -> verifyImpossible(actual)
            scored == 43 -> verifyImpossible(actual)
            scored == 41 -> verifyImpossible(actual)
            scored == 39 -> verifyImpossible(actual)
            scored == 37 -> verifyImpossible(actual)
            scored == 35 -> verifyImpossible(actual)
            scored == 31 -> verifyImpossible(actual)
            scored == 29 -> verifyImpossible(actual)
            scored == 23 -> verifyImpossible(actual)
            else -> verifyScore(scored, actual, 2)
        }
    }

    private fun verifyThreeDarts(scored: Int, actual: Turn) {
        when {
            scored >= 181 -> verifyImpossible(actual)
            scored == 179 -> verifyImpossible(actual)
            scored == 178 -> verifyImpossible(actual)
            scored == 176 -> verifyImpossible(actual)
            scored == 175 -> verifyImpossible(actual)
            scored == 173 -> verifyImpossible(actual)
            scored == 172 -> verifyImpossible(actual)
            scored == 169 -> verifyImpossible(actual)
            scored == 168 -> verifyImpossible(actual)
            scored == 166 -> verifyImpossible(actual)
            scored == 165 -> verifyImpossible(actual)
            scored == 163 -> verifyImpossible(actual)
            scored == 162 -> verifyImpossible(actual)
            scored == 159 -> verifyImpossible(actual)
            else -> verifyScore(scored, actual, 0)
        }
    }

    private fun verifyImpossible(actual: Turn) {
        assertEquals(0, actual.total())
    }

    private fun verifyScore(scored: Int, actual: Turn, minimumNumberOfDartsLeft: Int) {
        assertEquals(scored, actual.total())
        assertTrue(actual.dartsLeft() >= minimumNumberOfDartsLeft)
    }
}