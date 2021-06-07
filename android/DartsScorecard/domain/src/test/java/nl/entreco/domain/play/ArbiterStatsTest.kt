package nl.entreco.domain.play

import org.mockito.kotlin.whenever
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
 * Created by entreco on 23/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class ArbiterStatsTest{

    @Mock private lateinit var mockNext : Next
    private val givenStartIndex = 0
    private val givenTeams = arrayOf(Team(arrayOf(Player("1"))), Team(arrayOf(Player("2"))))
    private val givenScore = Score()
    private lateinit var subject : Arbiter

    @Test
    fun `it should keep track of turns`() {
        givenSubject()
        whenStarting()
        thenTurnCounterIs(0)
    }

    @Test
    fun `it should udpate turn counter after handling valid turn`() {
        givenSubject()
        whenStarting()
        whenHandling(Turn(Dart.SINGLE_1))
        thenTurnCounterIs(1)
    }

    @Test
    fun `it should NOT reset turn counter for new leg`() {
        givenSubject()
        whenStarting()
        whenHandling(Turn(Dart.SINGLE_1, Dart.TEST_D250))
        thenTurnCounterIs(1)
    }

    @Test
    fun `it should NOT increment turn counter when busting`() {
        givenSubject()
        whenStarting()
        whenHandling(Turn(Dart.TEST_501, Dart.TEST_D250, Dart.TEST_D250))
        thenTurnCounterIs(0)
    }

    @Test
    fun `it should track previous score`() {
        givenSubject()
        whenStarting()
        thenPreviousScoreIs(501)
    }

    @Test
    fun `it should track previous score after handling 1 turn`() {
        givenSubject()
        whenStarting()
        whenHandling(Turn(Dart.SINGLE_1))
        thenPreviousScoreIs(501)
    }

    @Test
    fun `it should track previous score when leg finished`() {
        givenSubject()
        whenStarting()
        whenHandling(Turn(Dart.SINGLE_1, Dart.TEST_D250))
        thenPreviousScoreIs(501)
    }

    private fun givenSubject() {
        subject = Arbiter(givenScore)
    }

    private fun whenStarting() {
        subject.start(givenStartIndex, givenTeams)
    }

    private fun whenHandling(turn: Turn) {
        whenever(mockNext.team).thenReturn(givenTeams[givenStartIndex])
        subject.handle(turn, mockNext)
    }

    private fun thenTurnCounterIs(expected: Int) {
        assertEquals(expected, subject.getTurnCount())
    }

    private fun thenPreviousScoreIs(expected: Int) {
        assertEquals(expected, subject.getPreviousScore().score)
    }
}