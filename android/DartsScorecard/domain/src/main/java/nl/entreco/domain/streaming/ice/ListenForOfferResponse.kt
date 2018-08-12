package nl.entreco.domain.streaming.ice

data class ListenForOfferResponse(val remoteUuid: String, val sessionType: Int, val sessionDescription: String)