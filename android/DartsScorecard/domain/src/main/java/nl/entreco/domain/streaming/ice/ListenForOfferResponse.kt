package nl.entreco.domain.streaming.ice

data class ListenForOfferResponse(val senderUuid: String, val sessionType: Int, val sessionDescription: String)