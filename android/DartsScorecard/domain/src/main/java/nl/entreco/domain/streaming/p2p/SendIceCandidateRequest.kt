package nl.entreco.domain.streaming.p2p

import nl.entreco.domain.streaming.ice.DscIceCandidate

data class SendIceCandidateRequest(val candidate: DscIceCandidate)