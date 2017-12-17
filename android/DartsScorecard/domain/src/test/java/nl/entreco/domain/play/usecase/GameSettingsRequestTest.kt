package nl.entreco.domain.play.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Entreco on 17/12/2017.
 */
class GameSettingsRequestTest {
    private val subject = GameSettingsRequest(1, 2, 3, 4)

    @Test
    fun `it should map correct values`() {
        assertEquals(1, subject.startScore)
        assertEquals(2, subject.startIndex)
        assertEquals(3, subject.numLegs)
        assertEquals(4, subject.numSets)
    }
}