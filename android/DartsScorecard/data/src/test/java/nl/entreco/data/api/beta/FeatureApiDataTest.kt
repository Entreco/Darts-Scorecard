package nl.entreco.data.api.beta

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 06/02/2018.
 */
class FeatureApiDataTest {
    private val description = "description"
    private val image = "some.image/com"
    private val title = "Some title"
    private val goal = 100
    private val count = 50
    private val subject = FeatureApiData(description, image, title, "", goal, count)

    @Test
    fun `description is empty initially`() {
        assertEquals(description, subject.description)
    }

    @Test
    fun `image is empty initially`() {
        assertEquals(image, subject.image)
    }

    @Test
    fun `title is empty initially`() {
        assertEquals(title, subject.title)
    }

    @Test
    fun `feature is empty initially`() {
        assertEquals("", subject.remco)
    }

    @Test
    fun `goal is zero initially`() {
        assertEquals(goal, subject.goal)
    }

    @Test
    fun `count is zero initially`() {
        assertEquals(count, subject.count)
    }

}
