package nl.entreco.domain.repository

import androidx.annotation.WorkerThread
import nl.entreco.domain.wtf.WtfItem

interface WtfRepository {
    @WorkerThread
    fun subscribe(onChange: (List<WtfItem>) -> Unit): List<WtfItem>

    @WorkerThread
    fun unsubscribe()

    @WorkerThread
    fun viewedItem(docId: String)
}