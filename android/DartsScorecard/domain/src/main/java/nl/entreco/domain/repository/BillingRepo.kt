package nl.entreco.domain.repository

import nl.entreco.domain.beta.donations.MakePurchaseResponse

interface BillingRepo {
    fun start()
    fun purchase(skuId: String)
    fun fetchPurchases()
    fun resume()
    fun stop()
}