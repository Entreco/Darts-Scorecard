package nl.entreco.domain.streaming.p2p

import nl.entreco.domain.streaming.ice.DscIceServer

data class ConnectToPeerRequest(val servers: List<DscIceServer>)