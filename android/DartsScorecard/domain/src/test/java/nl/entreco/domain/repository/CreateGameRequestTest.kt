package nl.entreco.domain.repository

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Entreco on 17/12/2017.
 */
class CreateGameRequestTest {
    private val subject = CreateGameRequest(1, 2, 3, 4)

    @Test
    fun `it should map correct values`() {
        assertEquals(1, subject.startScore)
        assertEquals(2, subject.startIndex)
        assertEquals(3, subject.numLegs)
        assertEquals(4, subject.numSets)
    }
}