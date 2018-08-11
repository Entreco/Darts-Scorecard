package nl.entreco.domain.streaming.ice

data class DscIceCandidate(val sdpMid: String, val sdpMLineIndex: Int, val sdp: String)