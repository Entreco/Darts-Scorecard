package nl.entreco.dartsscorecard.setup

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.Logger
import nl.entreco.domain.launch.TeamNamesString
import nl.entreco.domain.setup.game.CreateGameUsecase
import nl.entreco.domain.launch.usecase.ExtractTeamsUsecase
import nl.entreco.domain.setup.game.CreateGameRequest
import nl.entreco.domain.setup.game.CreateGameResponse
import nl.entreco.domain.repository.TeamIdsString
import javax.inject.Inject

/**
 * Created by Entreco on 20/12/2017.
 */
class Setup01ViewModel @Inject constructor(private val createGameUsecase: CreateGameUsecase, private val extractTeamsUsecase: ExtractTeamsUsecase, private val logger: Logger) : BaseViewModel() {

    fun onStartPressed(navigator: Setup01Navigator, setup: CreateGameRequest, teams: TeamNamesString) {
        ensureTeamPlayersExist(teams, {
            createNewGame(setup, it, onGameCreated(navigator), onGameCreatedFailed())
        }, onGameCreatedFailed())
    }

    private fun onGameCreated(navigator: Setup01Navigator): (CreateGameResponse) -> Unit =
            { req -> navigator.launch(req) }

    private fun onGameCreatedFailed(): (Throwable) -> Unit = { err ->
        logger.w("Unable to create game $err")
    }

    private fun ensureTeamPlayersExist(teamNamesInput: TeamNamesString, done: (TeamIdsString) -> Unit, fail: (Throwable) -> Unit) {
        extractTeamsUsecase.exec(teamNamesInput, done, fail)
    }

    private fun createNewGame(createGameRequest: CreateGameRequest, teamNames: TeamIdsString, done: (CreateGameResponse) -> Unit, fail: (Throwable) -> Unit) {
        createGameUsecase.exec(createGameRequest, teamNames, done, fail)
    }
}