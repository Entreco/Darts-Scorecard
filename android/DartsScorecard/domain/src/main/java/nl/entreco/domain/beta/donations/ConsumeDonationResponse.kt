package nl.entreco.domain.beta.donations

/**
 * Created by entreco on 09/02/2018.
 */
sealed class ConsumeDonationResponse {
    data class Success(val sku: String, val orderId: String) : ConsumeDonationResponse()
    data class Error(val sku: String, val errorCode: Int) : ConsumeDonationResponse()
}
