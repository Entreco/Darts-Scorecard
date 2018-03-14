package nl.entreco.dartsscorecard.beta

import nl.entreco.domain.beta.Feature
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 06/02/2018.
 */
class BetaModelTest {
    private val feature = Feature("reference", "title", "description", "http://url.com", "<b>remarks</b>", 10000, 500)
    private val subject = BetaModel(feature)

    @Test
    fun `it should set title`() {
        assertEquals("title", subject.title.get())
    }

    @Test
    fun `it should set total`() {
        assertEquals("10k", subject.total)
    }

    @Test
    fun `it should set progress`() {
        assertEquals(0.05F, subject.progress.get())
    }

    @Test
    fun `it should format remarks`() {
        assertEquals("<b>remarks</b>", subject.remarks.get())
    }
    @Test
    fun `it should set votable`() {
        assertEquals(true, subject.votable.get())
    }
    @Test
    fun `it should set description`() {
        assertEquals("description", subject.description.get())
    }
    @Test
    fun `it should set goal`() {
        assertEquals("500 / 10k", subject.goal.get())
    }
    @Test
    fun `it should set image`() {
        assertEquals("http://url.com", subject.image.get())
    }
}
