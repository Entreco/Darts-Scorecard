package nl.entreco.dartsscorecard.setup

import android.content.Context
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.domain.Logger
import nl.entreco.domain.launch.TeamNamesString
import nl.entreco.domain.launch.usecase.CreateGameUsecase
import nl.entreco.domain.launch.usecase.ExtractTeamsUsecase
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.TeamIdsString
import javax.inject.Inject

/**
 * Created by Entreco on 20/12/2017.
 */
class Setup01ViewModel @Inject constructor(private val createGameUsecase: CreateGameUsecase, private val extractTeamsUsecase: ExtractTeamsUsecase, private val logger: Logger) : BaseViewModel() {

    fun onStartPressed(context: Context, setup: CreateGameRequest, teams: TeamNamesString) {
        ensureTeamPlayersExist(teams, {
            createNewGame(setup, it, onGameCreated(context), onGameCreatedFailed())
        }, onGameCreatedFailed())
    }

    private fun onGameCreated(context: Context): (RetrieveGameRequest) -> Unit =
            { req -> Play01Activity.startGame(context, req) }

    private fun onGameCreatedFailed(): (Throwable) -> Unit = { err ->
        logger.w("Unable to create game $err")
    }

    private fun ensureTeamPlayersExist(teamNamesInput: TeamNamesString, done: (TeamIdsString) -> Unit, fail: (Throwable) -> Unit) {
        extractTeamsUsecase.exec(teamNamesInput, done, fail)
    }

    private fun createNewGame(createGameRequest: CreateGameRequest, teamNames: TeamIdsString, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        createGameUsecase.exec(createGameRequest, teamNames, done, fail)
    }
}