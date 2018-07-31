package nl.entreco.domain.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.play.description.MatchDescription

interface MatchDescriptionRepository {

    @WorkerThread
    fun fetchIt(): MatchDescription
}