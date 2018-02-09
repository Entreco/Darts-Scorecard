package nl.entreco.domain.beta.vote

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by entreco on 09/02/2018.
 */
class SubmitVoteResponseTest{
    @Test
    fun `report success`() {
        assertTrue(SubmitVoteResponse(true).ok)
    }

    @Test
    fun `report fail`() {
        assertFalse(SubmitVoteResponse(false).ok)
    }
}