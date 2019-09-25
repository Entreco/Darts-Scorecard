package nl.entreco.domain.beta.donations

/**
 * Created by entreco on 09/02/2018.
 */
sealed class MakeDonationResponse {
    data class Error(val code: Int? = -180) : MakeDonationResponse()
    data class Purchased(val purchaseToken: String, val productId: String, val orderId: String) : MakeDonationResponse()
    object Success : MakeDonationResponse()
    object Cancelled : MakeDonationResponse()
    object Pending : MakeDonationResponse()
    object AlreadyOwned : MakeDonationResponse()
}