package nl.entreco.dartsscorecard.splash

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.play.usecase.SetupModel
import nl.entreco.domain.play.usecase.CreateGameUsecase
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class SplashViewModel @Inject constructor(private val createGameUsecase: CreateGameUsecase) : BaseViewModel() {

    fun createGameIfNoneExists(setupModel: SetupModel, callback: CreateGameUsecase.Callback) {
        createGameUsecase.start(setupModel, callback)
    }
}