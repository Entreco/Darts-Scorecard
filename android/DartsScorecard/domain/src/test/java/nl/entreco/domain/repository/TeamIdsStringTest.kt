package nl.entreco.domain.repository

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Entreco on 17/12/2017.
 */
class TeamIdsStringTest {

    @Test
    fun `it should return correct string representation`() {
        assertEquals("1,2|3", TeamIdsString("1,2|3").toString())
    }

}