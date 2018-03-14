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
        assertEquals(0.05, subject.progress.get())
    }

    @Test
    fun `it should format remarks`() {
        assertEquals("<b>remarks</b>", subject.remarks.get())
    }
}
