package nl.entreco.dartsscorecard.beta

import nl.entreco.domain.beta.Feature
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 06/02/2018.
 */
class BetaModelTest {
    private val feat1 = Feature("reference", "title", "description", "http://url.com", "<b>remarks</b>", 10, 150, "")
    private val feat2 = Feature("reference", "title", "description", "http://url.com", "<b>remarks</b>", 100, 150, "")
    private val feat3 = Feature("reference", "title", "description", "http://url.com", "<b>remarks</b>", 500, 150, "")
    private val feat4 = Feature("reference", "title", "description", "http://url.com", "<b>remarks</b>", 1000, 150, "")
    private val feat5 = Feature("reference", "title", "description", "http://url.com", "<b>remarks</b>", 1500, 150, "")
    private val feat6 = Feature("reference", "title", "description", "http://url.com", "<b>remarks</b>", 5555, 150, "")
    private val feat7 = Feature("reference", "title", "description", "http://url.com", "<b>remarks</b>", 10000, 150, "")
    private val feat8 = Feature("reference", "title", "description", "http://url.com", "<b>remarks</b>", 12000, 150, "")
    private val feat9 = Feature("reference", "title", "description", "http://url.com", "<b>remarks</b>", 12345, 150, "")

    private val feature = Feature("reference", "title", "description", "http://url.com", "<b>remarks</b>", 1500, 150, "")
    private val subject = BetaModel(feature)

    @Test
    fun `it should set title`() {
        assertEquals("title", subject.title.get())
    }

    @Test
    fun `it should set total`() {
        assertEquals("1.5k", subject.total)
    }

    @Test
    fun `it should set total (without decimals)`() {
        assertEquals("10", BetaModel(feat1).total)
        assertEquals("100", BetaModel(feat2).total)
        assertEquals("500", BetaModel(feat3).total)
        assertEquals("1k", BetaModel(feat4).total)
        assertEquals("1.5k", BetaModel(feat5).total)
        assertEquals("5.6k", BetaModel(feat6).total)
        assertEquals("10k", BetaModel(feat7).total)
        assertEquals("12k", BetaModel(feat8).total)
        assertEquals("12.3k", BetaModel(feat9).total)
    }

    @Test
    fun `it should set progress`() {
        assertEquals(0.1F, subject.progress.get())
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
        assertEquals("150 / 1.5k", subject.goal.get())
    }

    @Test
    fun `it should set image`() {
        assertEquals("http://url.com", subject.image.get())
    }
}
