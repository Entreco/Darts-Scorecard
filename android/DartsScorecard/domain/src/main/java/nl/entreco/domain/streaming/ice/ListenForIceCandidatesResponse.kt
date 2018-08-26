package nl.entreco.domain.streaming.ice

data class ListenForIceCandidatesResponse(val candidate: DscIceCandidate, val shouldAdd: Boolean)