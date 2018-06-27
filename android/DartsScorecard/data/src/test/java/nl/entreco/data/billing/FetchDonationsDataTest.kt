package nl.entreco.data.billing

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by entreco on 09/02/2018.
 */
class FetchDonationsDataTest {
    @Test
    fun skuBundle() {
        assertNotNull(FetchDonationsData().skuBundle())
    }

    @Test
    fun `contains skus`() {
        assertTrue(FetchDonationsData().contains("10_feature_votes"))
        assertTrue(FetchDonationsData().contains("50_feature_votes"))
        assertTrue(FetchDonationsData().contains("100_feature_votes"))
        assertTrue(FetchDonationsData().contains("200_feature_votes"))
        assertTrue(FetchDonationsData().contains("500_feature_votes"))
        assertTrue(FetchDonationsData().contains("1000_feature_votes"))
    }

    @Test
    fun `should not contain weird ones`() {
        assertFalse(FetchDonationsData().contains(""))
        assertFalse(FetchDonationsData().contains("some other sku"))
        assertFalse(FetchDonationsData().contains(" "))
    }

    @Test
    fun `should have correct fetch type`() {
        assertEquals("inapp", FetchDonationsData().type())
    }

    @Test
    fun `should have correct purchase type`() {
        assertEquals("inapp", FetchPurchasesData().type())
    }

    @Test
    fun `should have correct purchase token`() {
        assertEquals("unused token", FetchPurchasesData().token())
    }

    @Test
    fun `should get correct votes`() {
        assertEquals(10, FetchDonationsData().getVotes("10_feature_votes"))
        assertEquals(50, FetchDonationsData().getVotes("50_feature_votes"))
        assertEquals(100, FetchDonationsData().getVotes("100_feature_votes"))
        assertEquals(200, FetchDonationsData().getVotes("200_feature_votes"))
        assertEquals(500, FetchDonationsData().getVotes("500_feature_votes"))
        assertEquals(1000, FetchDonationsData().getVotes("1000_feature_votes"))
    }

    @Test(expected = NoSuchElementException::class)
    fun `should return zero for weird ones (empty)`() {
        assertEquals(0, FetchDonationsData().getVotes(""))
    }

    @Test(expected = NoSuchElementException::class)
    fun `should return zero for weird ones (blank)`() {
        assertEquals(0, FetchDonationsData().getVotes(" "))
    }

    @Test(expected = NoSuchElementException::class)
    fun `should return zero for weird ones (not found)`() {
        assertEquals(0, FetchDonationsData().getVotes("some weird sku"))
    }

}