package nl.entreco.dartsscorecard.splash

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.play.usecase.CreateGameInput
import nl.entreco.domain.play.usecase.CreateGameUsecase
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class SplashViewModel @Inject constructor(private val createGameUsecase: CreateGameUsecase) : BaseViewModel() {

    fun createGameIfNoneExists(createGameInput: CreateGameInput, callback: CreateGameUsecase.Callback) {
        createGameUsecase.start(createGameInput, callback)
    }
}