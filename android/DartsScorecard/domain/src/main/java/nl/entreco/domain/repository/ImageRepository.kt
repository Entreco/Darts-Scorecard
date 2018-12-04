package nl.entreco.domain.repository

import androidx.annotation.WorkerThread

/**
 * Created by entreco on 11/03/2018.
 */
interface ImageRepository {

    @WorkerThread
    fun copyImageToPrivateAppData(imageUri: String?, size: Float): String?
}