package nl.entreco.data.api.beta

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 06/02/2018.
 */
class FeatureApiDataTest {
    private val subject = FeatureApiData()

    @Test
    fun `description is empty initially`() {
        assertEquals("", subject.description)
    }

    @Test
    fun `image is empty initially`() {
        assertEquals("", subject.image)
    }

    @Test
    fun `title is empty initially`() {
        assertEquals("", subject.title)
    }

    @Test
    fun `remarks is empty initially`() {
        assertEquals("", subject.remarks)
    }

    @Test
    fun `goal is zero initially`() {
        assertEquals(0, subject.goal)
    }

    @Test
    fun `count is zero initially`() {
        assertEquals(0, subject.count)
    }

}
