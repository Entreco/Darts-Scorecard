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
        assertEquals("20_feature_votes", Donate20Votes().productId)
        assertEquals("50_feature_votes", Donate50Votes().productId)
        assertEquals("100_feature_votes", Donate100Votes().productId)
        assertEquals("200_feature_votes", Donate200Votes().productId)
        assertEquals("500_feature_votes", Donate500Votes().productId)
        assertEquals("1000_feature_votes", Donate1000Votes().productId)
    }

    @Test
    fun getVotes() {
        assertEquals(10, Donate10Votes().votes)
        assertEquals(20, Donate20Votes().votes)
        assertEquals(50, Donate50Votes().votes)
        assertEquals(100, Donate100Votes().votes)
        assertEquals(200, Donate200Votes().votes)
        assertEquals(500, Donate500Votes().votes)
        assertEquals(1000, Donate1000Votes().votes)
    }


    @Test
    fun getProductIdsIncl() {
        assertEquals("10_remove_ads_votes", DonateAds10Votes().productId)
        assertEquals("20_remove_ads_votes", DonateAds20Votes().productId)
        assertEquals("50_remove_ads_votes", DonateAds50Votes().productId)
        assertEquals("100_remove_ads_votes", DonateAds100Votes().productId)
        assertEquals("200_remove_ads_votes", DonateAds200Votes().productId)
        assertEquals("500_remove_ads_votes", DonateAds500Votes().productId)
        assertEquals("1000_remove_ads_votes", DonateAds1000Votes().productId)
    }

    @Test
    fun getVotesIncl() {
        assertEquals(10, DonateAds10Votes().votes)
        assertEquals(20, DonateAds20Votes().votes)
        assertEquals(50, DonateAds50Votes().votes)
        assertEquals(100, DonateAds100Votes().votes)
        assertEquals(200, DonateAds200Votes().votes)
        assertEquals(500, DonateAds500Votes().votes)
        assertEquals(1000, DonateAds1000Votes().votes)
    }

}