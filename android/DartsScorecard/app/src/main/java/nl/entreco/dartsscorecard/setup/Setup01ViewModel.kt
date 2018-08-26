package nl.entreco.dartsscorecard.setup

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.shared.log.Logger
import nl.entreco.domain.launch.ExtractTeamsRequest
import nl.entreco.domain.launch.ExtractTeamsResponse
import nl.entreco.domain.launch.ExtractTeamsUsecase
import nl.entreco.domain.setup.game.CreateGameRequest
import nl.entreco.domain.setup.game.CreateGameResponse
import nl.entreco.domain.setup.game.CreateGameUsecase
import javax.inject.Inject

/**
 * Created by Entreco on 20/12/2017.
 */
class Setup01ViewModel @Inject constructor(private val createGameUsecase: CreateGameUsecase,
                                           private val extractTeamsUsecase: ExtractTeamsUsecase,
                                           private val logger: Logger) : BaseViewModel() {

    fun onStartPressed(navigator: Setup01Navigator,
                       setup: CreateGameRequest,
                       request: ExtractTeamsRequest) {
        try {
            request.validate()
            ensureTeamPlayersExist(request, {
                createNewGame(setup, it.teamNames, onGameCreated(navigator), onGameCreatedFailed())
            }, onGameCreatedFailed())
        } catch (err: Throwable) {
            logger.w("Unable to start new game: $err")
        }
    }

    private fun onGameCreated(navigator: Setup01Navigator): (CreateGameResponse) -> Unit =
            { req -> navigator.launch(req) }

    private fun onGameCreatedFailed(): (Throwable) -> Unit = { err ->
        logger.w("Unable to create game: $err")
    }

    private fun ensureTeamPlayersExist(teamNamesInput: ExtractTeamsRequest,
                                       done: (ExtractTeamsResponse) -> Unit,
                                       fail: (Throwable) -> Unit) {
        extractTeamsUsecase.exec(teamNamesInput, done, fail)
    }

    private fun createNewGame(createGameRequest: CreateGameRequest, teamNames: String,
                              done: (CreateGameResponse) -> Unit, fail: (Throwable) -> Unit) {
        createGameUsecase.exec(createGameRequest, teamNames, done, fail)
    }
}