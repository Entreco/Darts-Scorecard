package nl.entreco.data.wtf

import nl.entreco.data.api.beta.FeatureApiData
import org.junit.Assert.*
import org.junit.Test

class FaqApiDataTest{
    private val subject = FaqApiData()

    @Test
    fun `video is empty initially`() {
        assertEquals("", subject.video)
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
    fun `desc is empty initially`() {
        assertEquals("", subject.desc)
    }

    @Test
    fun `viewed is zero initially`() {
        assertEquals(0, subject.viewed)
    }
}