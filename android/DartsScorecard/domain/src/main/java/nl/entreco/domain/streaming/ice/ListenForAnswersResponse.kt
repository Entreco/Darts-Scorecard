package nl.entreco.domain.streaming.ice

data class ListenForAnswersResponse(val remoteUuid: String, val sessionType: Int, val sessionDescription: String)