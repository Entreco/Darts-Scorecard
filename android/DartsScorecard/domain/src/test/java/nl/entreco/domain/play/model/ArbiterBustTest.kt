package nl.entreco.domain.play.model

import nl.entreco.domain.play.TestProvider
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.State
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.settings.ScoreSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Created by Entreco on 05/12/2017.
 */
class ArbiterBustTest {

    private val startIndex = 0
    private val initialScore = Score(40, 0, 0, ScoreSettings(40, 1, 1, startIndex))
    private val turnHandler = TurnHandler(startIndex).also { it.teams = TestProvider().teams() }
    val subject: Arbiter = Arbiter(initialScore, turnHandler)

    @Test
    fun `it should update Next() when busting with state==BUST`() {
        val expected = subject.start()
        val actual = subject.handle(Turn(Dart.TEST_501, Dart.TEST_501, Dart.TEST_501), expected)
        assertNotEquals(expected, actual)
        assertEquals(actual.state, State.ERR_BUST)
    }

    @Test
    fun `it should update Next() when leaving 1 with state==ERR_BUST`() {
        val expected = subject.start()
        val actual = subject.handle(Turn(Dart.SINGLE_4, Dart.SINGLE_15, Dart.SINGLE_20), expected)
        assertNotEquals(expected, actual)
        assertEquals(actual.state, State.ERR_BUST)
    }

    @Test
    fun `it should update Next() when not finishing with double with state==ERR_REQUIRES_DOUBLE`() {
        val expected = subject.start()
        val actual = subject.handle(Turn(Dart.SINGLE_5, Dart.SINGLE_15, Dart.SINGLE_20), expected)
        assertNotEquals(expected, actual)
        assertEquals(actual.state, State.ERR_REQUIRES_DOUBLE)
    }

    @Test
    fun `it should update Next() when finishing with S2 D19`() {
        val expected = subject.start()
        val actual = subject.handle(Turn(Dart.SINGLE_2, Dart.DOUBLE_19), expected)
        assertNotEquals(expected, actual)
        assertEquals(State.MATCH, actual.state)
    }

    @Test
    fun `it should update Next() when finishing with S2 S20 D9`() {
        val expected = subject.start()
        val actual = subject.handle(Turn(Dart.SINGLE_2, Dart.SINGLE_20, Dart.DOUBLE_9), expected)
        assertNotEquals(expected, actual)
        assertEquals(State.MATCH, actual.state)
    }

    @Test
    fun `it should update Next() when finishing with D20`() {
        val expected = subject.start()
        val actual = subject.handle(Turn(Dart.DOUBLE_20), expected)
        assertNotEquals(expected, actual)
        assertEquals(State.MATCH, actual.state)
    }
}