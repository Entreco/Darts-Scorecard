package nl.entreco.domain.play.mastercaller

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 14/03/2018.
 */
class MasterCallerRequestTest{

    @Test
    fun `it should map to 'Fx01' when scoring 1`() {
        assertEquals(Fx01(), req(1))
    }

    @Test
    fun `it should map to 'None' for unknown scores`() {
        assertEquals(None(), req(181))
    }

    private fun req(scored: Int) = MasterCallerRequest(scored).toSound()
}