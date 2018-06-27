package nl.entreco.data.billing

import android.os.Bundle
import nl.entreco.domain.beta.Donation
import java.util.*

/**
 * Created by entreco on 08/02/2018.
 */
sealed class BillingData(val productId: String, val votes: Int)

const val type = "inapp"
internal val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+/:".toCharArray()

class Donate10Votes : BillingData("10_feature_votes", 10)
class Donate20Votes : BillingData("20_feature_votes", 20)
class Donate50Votes : BillingData("50_feature_votes", 50)
class Donate100Votes : BillingData("100_feature_votes", 100)
class Donate200Votes : BillingData("200_feature_votes", 200)
class Donate500Votes : BillingData("500_feature_votes", 500)
class Donate1000Votes : BillingData("1000_feature_votes", 1000)

class DonateAds10Votes : BillingData("10_remove_ads_votes", 10)
class DonateAds20Votes : BillingData("20_remove_ads_votes", 20)
class DonateAds50Votes : BillingData("50_remove_ads_votes", 50)
class DonateAds100Votes : BillingData("100_remove_ads_votes", 100)
class DonateAds200Votes : BillingData("200_remove_ads_votes", 200)
class DonateAds500Votes : BillingData("500_remove_ads_votes", 500)
class DonateAds1000Votes : BillingData("1000_remove_ads_votes", 1000)

class DonateTestPurchased : BillingData("android.test.purchased", 10)
class DonateTestCancelled : BillingData("android.test.canceled", 10)
class DonateTestRefunded : BillingData("android.test.refunded", 10)
class DonateTestUnavailable : BillingData("android.test.item_unavailable", 10)

sealed class InAppProducts(private val billingData: List<BillingData>) {
    private fun listOfDonations(): List<BillingData> {
        return billingData
    }

    private fun listOfProducts(): List<String> {
        return listOfDonations().map { it.productId }
    }

    fun skuBundle(): Bundle {
        val skuBundle = Bundle()
        skuBundle.putStringArrayList("ITEM_ID_LIST", ArrayList(listOfProducts()))
        return skuBundle
    }

    fun contains(sku: String): Boolean {
        return listOfProducts().contains(sku)
    }

    fun type(): String {
        return type
    }

    fun getVotes(productId: String): Int {
        return listOfDonations().first { it.productId == productId }.votes
    }
}

class FetchDonationsInclAdsData : InAppProducts(listOf(DonateAds10Votes(), DonateAds20Votes(), DonateAds50Votes(), DonateAds100Votes(), DonateAds200Votes(), DonateAds500Votes(), DonateAds1000Votes()))
class FetchDonationsData : InAppProducts(listOf(Donate10Votes(), Donate20Votes(), Donate50Votes(), Donate100Votes(), Donate200Votes(), Donate500Votes(), Donate1000Votes()))
class FetchDonationsTestData : InAppProducts(listOf(DonateTestPurchased(), DonateTestCancelled(), DonateTestRefunded(), DonateTestUnavailable()))
class FetchDonationsInclAdsTestData : InAppProducts(listOf(DonateTestPurchased(), DonateTestCancelled(), DonateTestRefunded(), DonateTestUnavailable()))

class MakeDonationData(private val donation: Donation) {

    fun sku(): String {
        return donation.sku
    }

    fun type(): String {
        return type
    }

    fun payload(): String {
        return (0..100).joinToString { chars[Random().nextInt(chars.size - 1)].toString() }
    }
}

class FetchPurchasesData {
    fun type(): String {
        return type
    }

    fun token(): String {
        return "unused token"
    }
}

