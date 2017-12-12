package nl.entreco.domain.play.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.usecase.SetupModel

/**
 * Created by Entreco on 15/11/2017.
 */
interface GameRepository {

    @Throws
    @WorkerThread
    fun create(createModel: SetupModel): Game

    @Throws
    @WorkerThread
    fun fetchBy(uid: String): Game

    @Throws
    @WorkerThread
    fun fetchLatest(): Game
}