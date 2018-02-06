package nl.entreco.dartsscorecard.beta

import nl.entreco.domain.beta.Feature
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 06/02/2018.
 */
class BetaModelTest{
    private val feature = Feature("title", "description", "http://url.com", 10000, 500)
    private val subject = BetaModel(feature)

    @Test
    fun `it should set title`() {
        assertEquals("title", subject.title.get())
    }
}