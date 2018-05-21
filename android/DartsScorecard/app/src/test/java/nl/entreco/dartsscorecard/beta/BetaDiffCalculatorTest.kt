package nl.entreco.dartsscorecard.beta

import nl.entreco.domain.beta.Feature
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by entreco on 19/02/2018.
 */
class BetaDiffCalculatorTest {

    private val feature1 = Feature("ref1", "title1", "desc1", "img1", "update", 1, 1, "")
    private val feature2 = Feature("ref2", "title2", "desc2", "img2", "update", 2, 2, "")
    private val feature3 = Feature("ref3", "title3", "desc3", "img3", "update", 3, 3, "")
    private val feature4 = Feature("ref4", "title4", "desc4", "img4", "update", 4, 4, "")

    @Test
    fun `areItemsTheSame true`() {
        val subject = BetaDiffCalculator(listOf(feature1), listOf(feature1))
        assertTrue(subject.areItemsTheSame(0, 0))
    }

    @Test
    fun `areItemsTheSame false`() {
        val subject = BetaDiffCalculator(listOf(feature1), listOf(feature3))
        assertFalse(subject.areItemsTheSame(0, 0))
    }

    @Test
    fun getOldListSize() {
        val subject = BetaDiffCalculator(listOf(feature1, feature2), listOf(feature3, feature4))
        assertEquals(2, subject.oldListSize)
    }

    @Test
    fun getNewListSize() {
        val subject = BetaDiffCalculator(listOf(feature1, feature2), listOf(feature3, feature4))
        assertEquals(2, subject.newListSize)
    }

    @Test
    fun `areContentsTheSame true`() {
        val subject = BetaDiffCalculator(listOf(feature1, feature2), listOf(feature1, feature4))
        assertTrue(subject.areContentsTheSame(0, 0))
    }

    @Test
    fun `areContentsTheSame false`() {
        val subject = BetaDiffCalculator(listOf(feature1, feature2), listOf(feature1, feature4))
        assertFalse(subject.areContentsTheSame(1, 1))
    }

}
