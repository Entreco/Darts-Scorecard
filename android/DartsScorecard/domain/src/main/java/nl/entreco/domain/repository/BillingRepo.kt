package nl.entreco.domain.repository

import nl.entreco.domain.beta.donations.MakePurchaseResponse

interface BillingRepo {
    fun start()
    fun stop()
    fun purchase(skuId: String)
    fun fetchPurchases()
    fun resume()
}