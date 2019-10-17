package nl.entreco.domain.beta.donations

data class Purchase(
        val sku: String,
        val acknolwdged: Boolean,
        val state: Int
)