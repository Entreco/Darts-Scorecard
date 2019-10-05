package nl.entreco.domain.beta.donations

/**
 * Created by entreco on 09/02/2018.
 */
data class ConsumeDonationRequest(val purchaseToken: String,
                                  val sku: String,
                                  val orderId: String,
                                  val requiresConsumption: Boolean)