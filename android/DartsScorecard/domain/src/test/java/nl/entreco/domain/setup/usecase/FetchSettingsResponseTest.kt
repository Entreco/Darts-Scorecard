package nl.entreco.domain.setup.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 04/01/2018.
 */
class FetchSettingsResponseTest{

    private val subject = FetchSettingsResponse()

    @Test
    fun `default number of sets should be 1`() {
        assertEquals(1, subject.startSets)
    }

    @Test
    fun `default number of legs should be 3`() {
        assertEquals(3, subject.startLegs)
    }

    @Test
    fun `default minimum number for best of should be 0`() {
        assertEquals(0, subject.min)
    }

    @Test
    fun `default maximum number for best of should be 20`() {
        assertEquals(20, subject.max)
    }

    @Test
    fun `default startScore should be 501`() {
        assertEquals(501, subject.startScore)
    }

}