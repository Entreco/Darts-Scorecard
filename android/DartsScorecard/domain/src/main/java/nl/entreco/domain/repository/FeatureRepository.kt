package nl.entreco.domain.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.beta.Feature

/**
 * Created by entreco on 03/02/2018.
 */
interface FeatureRepository {
    @WorkerThread
    fun fetchAll(onChange: (List<Feature>)->Unit): List<Feature>
}