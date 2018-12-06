package nl.entreco.domain.repository

import androidx.annotation.WorkerThread
import nl.entreco.domain.hiscores.HiScore

interface HiScoreRepository {
    @WorkerThread
    fun fetchHiscores(): List<HiScore>
}