package nl.entreco.domain.streaming.ice

data class DscSessionDescription(val uuid: String,
                                 val type: Int,
                                 val description: String)