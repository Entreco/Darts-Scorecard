package nl.entreco.data.billing

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 09/02/2018.
 */
class BillingDataTest {
    @Test
    fun getProductIds() {
        assertEquals("10_feature_votes", Donate10Votes().productId)
        assertEquals("50_feature_votes", Donate50Votes().productId)
        assertEquals("100_feature_votes", Donate100Votes().productId)
    }

    @Test
    fun getVotes() {
        assertEquals(10, Donate10Votes().votes)
        assertEquals(50, Donate50Votes().votes)
        assertEquals(100, Donate100Votes().votes)
    }

}