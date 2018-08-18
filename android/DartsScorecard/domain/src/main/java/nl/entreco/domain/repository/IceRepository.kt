package nl.entreco.domain.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.streaming.ice.DscIceCandidate
import nl.entreco.domain.streaming.ice.DscIceServer

interface IceRepository {
    @WorkerThread
    fun fetchIceServers(done: (List<DscIceServer>) -> Unit)

    @WorkerThread
    fun listenForIceCandidates(remoteUuid: String, add:(DscIceCandidate)->Unit, remove: (DscIceCandidate)->Unit)

    @WorkerThread
    fun send(candidate: DscIceCandidate, done:(Boolean)->Unit)

    @WorkerThread
    fun remove(iceCandidatesToRemove: Array<DscIceCandidate>, done: (Boolean)->Unit)
}