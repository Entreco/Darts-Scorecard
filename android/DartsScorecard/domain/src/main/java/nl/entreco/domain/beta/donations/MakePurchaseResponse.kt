package nl.entreco.domain.beta.donations

import nl.entreco.domain.beta.Donation

/**
 * Created by entreco on 09/02/2018.
 */
sealed class MakePurchaseResponse {
    data class Error(val code: Int) : MakePurchaseResponse()
    data class Purchased(val donation: Donation, val orderId: String) : MakePurchaseResponse()
    data class Pending(val sku: String) : MakePurchaseResponse()
    data class Updated(val purchases: List<Purchase>): MakePurchaseResponse()
    data class Donations(val donations: List<Donation>): MakePurchaseResponse()
    object Unavailable : MakePurchaseResponse()
    object Connected : MakePurchaseResponse()
    object Disconnected : MakePurchaseResponse()
    object Consumed : MakePurchaseResponse()
    object Acknowledged : MakePurchaseResponse()
    object Cancelled : MakePurchaseResponse()
    object Launched : MakePurchaseResponse()
    object Unknown : MakePurchaseResponse()
}