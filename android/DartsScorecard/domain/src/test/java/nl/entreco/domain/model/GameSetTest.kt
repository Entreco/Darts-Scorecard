package nl.entreco.domain.model

import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.play.Arbiter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GameSetTest {

    @Mock private lateinit var mockArbiter: Arbiter
    private lateinit var subject: Game

    @Test
    fun `it should return 0 when starting`() {
        givenScores(Score(501, 0, 0))
        thenSetCounterIs(0)
    }

    @Test
    fun `it should return 0 when starting (2 players)`() {
        givenScores(Score(501, 0, 0), Score(501, 0, 0))
        thenSetCounterIs(0)
    }

    @Test
    fun `it should return 0 when starting (3 players)`() {
        givenScores(Score(501, 0, 0), Score(501, 0, 0), Score(501, 0, 0))
        thenSetCounterIs(0)
    }

    @Test
    fun `it should return 0 when scoring one`() {
        givenScores(Score(500, 0, 0))
        thenSetCounterIs(0)
    }

    @Test
    fun `it should return 0 when finishing leg`() {
        givenScores(Score(501, 0, 1))
        thenSetCounterIs(0)
    }

    @Test
    fun `it should return 1 when scoring after 1st leg`() {
        givenScores(Score(500, 0, 1))
        thenSetCounterIs(1)
    }

    @Test
    fun `it should return 1 after 1st leg in 2nd set`() {
        givenScores(Score(501, 1, 1))
        thenSetCounterIs(1)
    }

    @Test
    fun `it should return 1 when scoring after 1st leg in 2nd set`() {
        givenScores(Score(500, 1, 1))
        thenSetCounterIs(1)
    }

    @Test
    fun `it should return 0 after 3rd leg in 2nd set (2 players)`() {
        givenScores(Score(501, 1, 0), Score(501, 1, 0))
        thenSetCounterIs(0)
    }

    @Test
    fun `it should return 0 after 1st leg in 2nd set (2 players)`() {
        givenScores(Score(501, 0, 1), Score(501, 0, 0))
        thenSetCounterIs(0)
    }

    @Test
    fun `it should return 1 after 1st leg in 2nd set (2 players)`() {
        givenScores(Score(501, 0, 1), Score(500, 0, 0))
        thenSetCounterIs(1)
    }

    @Test
    fun `it should return 2 after 1st leg in 2nd set (2 players)`() {
        givenScores(Score(501, 1, 1), Score(501, 1, 1))
        thenSetCounterIs(2)
    }

    @Test
    fun `it should return 2 when scoring after 1st leg in 2nd set (2 players)`() {
        givenScores(Score(500, 1, 1), Score(501, 1, 1))
        thenSetCounterIs(2)
    }

    @Test
    fun `it should return 1 when scoring after 1st leg (2 players)`() {
        givenScores(Score(500, 0, 1), Score(501, 0, 0))
        thenSetCounterIs(1)
    }

    @Test
    fun `it should return 1 when scoring after 1st leg (3 players)`() {
        givenScores(Score(500, 0, 1), Score(501, 0, 0), Score(501, 0, 0))
        thenSetCounterIs(1)
    }

    @Test
    fun `it should return 1 when scoring (multiple) after 1st leg`() {
        givenScores(Score(1, 0, 1))
        thenSetCounterIs(1)
    }

    @Test
    fun `it should return 0 when after 1 sets`() {
        givenScores(Score(501, 0, 0), Score(501, 0, 1))
        thenSetCounterIs(0)
    }

    @Test
    fun `it should return 1 when after 2 sets`() {
        givenScores(Score(501, 0, 1), Score(501, 0, 1))
        thenSetCounterIs(1)
    }

    @Test
    fun `it should return 2 when after 3 sets`() {
        givenScores(Score(501, 0, 1), Score(501, 0, 2))
        thenSetCounterIs(2)
    }

    @Test
    fun `it should return 3 when after 4 sets`() {
        givenScores(Score(501, 0, 2), Score(501, 0, 2))
        thenSetCounterIs(3)
    }

    @Test
    fun `it should return 4 when scoring (multiple) after 4 sets`() {
        givenScores(Score(501, 0, 2), Score(500, 0, 2))
        thenSetCounterIs(4)
    }

    private fun givenScores(vararg scores: Score) {
        subject = Game(0, mockArbiter)
        whenever(mockArbiter.getScores()).thenReturn(scores.map { it }.toTypedArray())
    }

    private fun thenSetCounterIs(expected: Int) {
        assertEquals(expected, subject.getSetCount())
    }
}