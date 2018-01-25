package nl.entreco.domain.launch

import org.junit.Assert
import org.junit.Test

/**
 * Created by entreco on 06/01/2018.
 */
class ExtractTeamsResponseTest {
    @Test
    fun `it should return correct string representation`() {
        Assert.assertEquals("1,2|3", ExtractTeamsResponse("1,2|3").teamNames)
    }
}