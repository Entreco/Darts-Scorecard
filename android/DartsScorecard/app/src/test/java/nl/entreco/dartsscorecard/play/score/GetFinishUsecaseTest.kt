package nl.entreco.dartsscorecard.play.score

import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.usecase.GetFinishUsecase
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Entreco on 24/11/2017.
 */
class GetFinishUsecaseTest {

    private val subject = GetFinishUsecase()

    @Test
    fun `it should return empty for scores higher than 170`() {
        assertEquals("", subject.calculateInBack(171, Turn(), 20))
    }

    @Test
    fun `it should return T20 T20 BULL for score of 170`() {
        assertEquals("T20 T20 BULL", subject.calculateInBack(170, Turn(), 20))
    }
}