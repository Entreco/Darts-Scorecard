package nl.entreco.domain.beta.donations

/**
 * Created by entreco on 09/02/2018.
 */
sealed class MakePurchaseResponse {
    data class Error(val code: Int) : MakePurchaseResponse()
    data class Purchased(val purchaseToken: String, val sku: String, val orderId: String) : MakePurchaseResponse()
    object Consumed : MakePurchaseResponse()
    object Acknowledged : MakePurchaseResponse()
    object Cancelled : MakePurchaseResponse()
    object Pending : MakePurchaseResponse()
}