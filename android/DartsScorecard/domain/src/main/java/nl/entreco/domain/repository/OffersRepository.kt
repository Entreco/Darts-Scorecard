package nl.entreco.domain.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.streaming.ice.DscSessionDescription

interface OffersRepository {

    @WorkerThread
    fun create(localSessionDescription: DscSessionDescription)

    @WorkerThread
    fun listenForNewOffersWithUuid(onChange:(DscSessionDescription)->Unit)


}