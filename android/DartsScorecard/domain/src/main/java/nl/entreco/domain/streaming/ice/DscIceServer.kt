package nl.entreco.domain.streaming.ice

data class DscIceServer(val uri: String,
                        val username: String? = null,
                        val password: String? = null)