package nl.entreco.data.stream

import nl.entreco.domain.streaming.ice.DscIceCandidate
import nl.entreco.domain.streaming.ice.DscIceServer

internal class IceCandidateMapper {
    internal fun map(servers: ArrayList<IceServerFirebaseApiData>?): List<DscIceServer> {
        return servers?.mapNotNull { map(it) } ?: emptyList()
    }

    private fun map(apiData: IceServerFirebaseApiData): DscIceServer? {
        return if (apiData.uri?.isNotBlank() == true) {
            DscIceServer(apiData.uri)
        } else {
            null
        }
    }

    internal fun map(apiData: IceCandidateFirebaseApiData): DscIceCandidate {
        return DscIceCandidate(apiData.sdpMid, apiData.sdpMLineIndex, apiData.sdp)
    }

    internal fun map(data: DscIceCandidate) : IceCandidateFirebaseApiData {
        val candidate = IceCandidateFirebaseApiData()
        candidate.sdp = data.sdp
        candidate.sdpMLineIndex = data.sdpMLineIndex
        candidate.sdpMid = data.sdpMid
        return candidate
    }

    internal fun map(data: Array<DscIceCandidate>) : List<IceCandidateFirebaseApiData> {
        return data.map { map(it) }
    }
}