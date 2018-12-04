package nl.entreco.domain.repository

import androidx.annotation.WorkerThread
import nl.entreco.domain.beta.Feature

/**
 * Created by entreco on 03/02/2018.
 */
interface FeatureRepository {
    @WorkerThread
    fun subscribe(onChange: (List<Feature>)->Unit): List<Feature>

    @WorkerThread
    fun unsubscribe()

    @WorkerThread
    fun submitVote(featureId: String, amount: Int)
}