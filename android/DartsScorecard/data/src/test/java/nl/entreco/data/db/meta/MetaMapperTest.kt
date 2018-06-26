package nl.entreco.data.db.meta

import nl.entreco.domain.model.Score
import nl.entreco.domain.model.TurnMeta
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 22/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class MetaMapperTest {

    @Mock
    private lateinit var mockTurnMeta: TurnMeta

    private val givenGameId: Long = 32
    private val givenTurnId: Long = 1688
    private val givenAtDouble: Int = 800


    private val givenPlayerId: Long = 33
    private val givenTurnNumber: Int = 11
    private val givenScore: Score = Score(102, 4, 5)
    private val givenBreakMade = true

    private val givenMeta: TurnMeta = TurnMeta(givenPlayerId, givenTurnNumber, givenScore, givenBreakMade)
    private val subject = MetaMapper()

    @Test
    fun from() {
        val result = subject.from(givenGameId, givenTurnId, givenMeta, givenAtDouble)
        assertEquals(0, result.id)
        assertEquals(givenTurnId, result.turnId)
        assertEquals(givenGameId, result.gameId)
        assertEquals(givenPlayerId, result.playerId)
        assertEquals(givenScore.leg, result.legNumber)
        assertEquals(givenScore.set, result.setNumber)
        assertEquals(givenScore.score, result.score)
        assertEquals(givenTurnNumber, result.turnInLeg)
        assertEquals(givenAtDouble, result.atCheckout)
        assertEquals(true, result.breakMade)
    }

    @Test(expected = NotImplementedError::class)
    fun `to not implemented`() {
        subject.to(mockTurnMeta)
    }
}