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
class Donate50Votes : BillingData("50_feature_votes", 50)
class Donate100Votes : BillingData("100_feature_votes", 100)
class Donate500Votes : BillingData("500_feature_votes", 500)
class DonateTestPurchased : BillingData("android.test.purchased", 10)
class DonateTestCancelled : BillingData("android.test.canceled", 10)
class DonateTestRefunded : BillingData("android.test.refunded", 10)
class DonateTestUnavailable : BillingData("android.test.item_unavailable", 10)

class FetchDonationsData {

    private fun listOfDonations(): List<BillingData> {
        return listOf(Donate10Votes(), Donate50Votes(), Donate100Votes(), Donate500Votes())
//        return listOf(DonateTestPurchased(), DonateTestCancelled(), DonateTestRefunded(), DonateTestUnavailable())
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

