package nl.entreco.data.stream

import com.google.firebase.database.*
import nl.entreco.domain.repository.IceRepository
import nl.entreco.domain.streaming.ice.DscIceCandidate
import nl.entreco.domain.streaming.ice.DscIceServer
import nl.entreco.shared.log.Logger

class FirebaseIceRepository(private val db: FirebaseDatabase,
                            private val logger: Logger) : IceRepository {

    companion object {
        private const val ICE_SERVERS_PATH = "ice_servers"
        private const val ICE_CANDIDATES_PATH = "ice_candidates/"
    }

    private fun deviceIceCandidatesPath(uuid: String) = ICE_CANDIDATES_PATH.plus(uuid)

    override fun fetchIceServers(done: (List<DscIceServer>) -> Unit) {
        db.getReference(ICE_SERVERS_PATH)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        done(emptyList())
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val genericTypeIndicator = object :
                                GenericTypeIndicator<ArrayList<IceServerFirebaseApiData>>() {}
                        val servers = p0.getValue(genericTypeIndicator)
                        done(servers?.mapNotNull { toIce(it) } ?: emptyList())
                    }
                })
    }

    private fun toIce(apiData: IceServerFirebaseApiData): DscIceServer? {
        return if (apiData.uri?.isNotBlank() == true) {
            DscIceServer(apiData.uri)
        } else {
            null
        }
    }

    override fun listenForIceCandidates(remoteUuid: String, add: (DscIceCandidate) -> Unit,
                                        remove: (DscIceCandidate) -> Unit) {
        db.getReference(deviceIceCandidatesPath(remoteUuid))
                .addChildEventListener(object : ChildEventListener {
                    override fun onCancelled(p0: DatabaseError) {}

                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

                    override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        p0.getValue(IceCandidateFirebaseApiData::class.java)?.let {
                            add(toIce(it))
                        }
                    }

                    override fun onChildRemoved(p0: DataSnapshot) {
                        p0.getValue(IceCandidateFirebaseApiData::class.java)?.let {
                            remove(toIce(it))
                        }
                    }
                })
    }

    private fun toIce(apiData: IceCandidateFirebaseApiData): DscIceCandidate {
        return DscIceCandidate(apiData.sdpMid, apiData.sdpMLineIndex, apiData.sdp)
    }
}