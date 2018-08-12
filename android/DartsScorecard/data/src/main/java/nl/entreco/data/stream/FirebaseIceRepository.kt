package nl.entreco.data.stream

import com.google.firebase.database.*
import nl.entreco.domain.repository.IceRepository
import nl.entreco.domain.streaming.ice.DscIceCandidate
import nl.entreco.domain.streaming.ice.DscIceServer
import nl.entreco.shared.log.Logger

class FirebaseIceRepository(private val db: FirebaseDatabase,
                            private val logger: Logger,
                            private val currentDeviceUuid: String) : IceRepository {

    companion object {
        private const val ICE_SERVERS_PATH = "ice_servers"
        private const val ICE_CANDIDATES_PATH = "ice_candidates/"
    }

    private val mapper by lazy { IceCandidateMapper() }
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
                        done(mapper.map(servers))
                    }
                })
    }

    override fun send(candidate: DscIceCandidate, done:(Boolean)->Unit) {
        val reference = db.getReference(deviceIceCandidatesPath(currentDeviceUuid))
        with(reference) {
            onDisconnect().removeValue()
            push().setValue(mapper.map(candidate)){ dbError, _ ->
                if(dbError != null){
                    done(false)
                } else {
                    done(true)
                }
            }
        }
    }

    override fun remove(iceCandidatesToRemove: Array<DscIceCandidate>, done: (Boolean) -> Unit) {
        val iceCandidatesToRemoveList = mapper.map(iceCandidatesToRemove).toMutableList()
        val reference = db.getReference(deviceIceCandidatesPath(currentDeviceUuid))

        reference.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val typeIndicator = object : GenericTypeIndicator<MutableMap<String, IceCandidateFirebaseApiData>>() {}
                val currentIceCandidatesInFirebaseMap = mutableData.getValue(typeIndicator) ?:
                return Transaction.success(mutableData)


                for ((key, value) in currentIceCandidatesInFirebaseMap) {
                    if (iceCandidatesToRemoveList.remove(value)) {
                        currentIceCandidatesInFirebaseMap.remove(key)
                    }
                }
                mutableData.value = currentIceCandidatesInFirebaseMap
                return Transaction.success(mutableData)
            }

            override fun onComplete(databaseError: DatabaseError?, committed: Boolean, p2: DataSnapshot?) {
                if (committed) {
                    done(true)
                } else {
                    done(false)
                }
            }
        })
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
                            add(mapper.map(it))
                        }
                    }

                    override fun onChildRemoved(p0: DataSnapshot) {
                        p0.getValue(IceCandidateFirebaseApiData::class.java)?.let {
                            remove(mapper.map(it))
                        }
                    }
                })
    }
}