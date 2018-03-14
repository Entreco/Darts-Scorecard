package nl.entreco.domain.play.mastercaller

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by entreco on 14/03/2018.
 */
class MasterCallerRequestTest{

    @Test
    fun `it should map to 'Fx01' when scoring 1`() {
        assertTrue( req(1) is Fx01)
    }

    @Test
    fun `it should map to 'None' for unknown scores`() {
        assertTrue(req(181) is None)
    }

    private fun req(scored: Int) = MasterCallerRequest(scored).toSound()
}