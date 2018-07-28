package nl.entreco.domain.profile.fetch

import org.junit.Test

import org.junit.Assert.*

class FetchProfileStatRequestTest {

    @Test
    fun `should be equal`() {
        val one = FetchProfileStatRequest(longArrayOf(1,2,34))
        val two = FetchProfileStatRequest(longArrayOf(1,2,34))
        assertEquals(one, two)
    }

    @Test
    fun `should not be equal`() {
        val one = FetchProfileStatRequest(longArrayOf(1,2,34))
        val two = FetchProfileStatRequest(longArrayOf(1,2,3,4))
        assertNotEquals(one, two)
    }
}