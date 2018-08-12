package nl.entreco.domain.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.streaming.ice.DscSessionDescription

interface AnswersRepository {
    @WorkerThread
    fun create(localSessionDescription: DscSessionDescription)

    @WorkerThread
    fun listenForNewAnswers(onChange:(DscSessionDescription)->Unit)
}