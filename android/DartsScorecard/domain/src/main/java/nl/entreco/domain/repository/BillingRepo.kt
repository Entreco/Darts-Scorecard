package nl.entreco.domain.repository

interface BillingRepo {
    fun start()
    fun purchase(skuId: String)
    fun fetchPurchases()
    fun resume()
    fun stop()
}