package nl.entreco.domain.streaming.p2p

import nl.entreco.domain.streaming.ice.DscIceCandidate

data class RemoveIceCandidateRequest(val candidates: Array<DscIceCandidate>)