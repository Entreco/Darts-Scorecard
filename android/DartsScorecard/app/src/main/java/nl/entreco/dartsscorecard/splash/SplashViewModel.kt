package nl.entreco.dartsscorecard.splash

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.model.players.TeamIdsString
import nl.entreco.domain.splash.TeamNamesString
import nl.entreco.domain.splash.usecase.CreateGameUsecase
import nl.entreco.domain.splash.usecase.CreateTeamsUsecase
import nl.entreco.domain.play.usecase.GameSettingsRequest
import nl.entreco.domain.play.usecase.RetrieveGameRequest
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class SplashViewModel @Inject constructor(private val createGameUsecase: CreateGameUsecase, private val createTeamsUsecase: CreateTeamsUsecase) : BaseViewModel() {


    fun createFrom(teams: TeamNamesString, request: GameSettingsRequest, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        ensureTeamPlayersExist(teams,
                handleTeamRetrieved(request, done, fail),
                fail)
    }

    private fun handleTeamRetrieved(request: GameSettingsRequest, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit): (TeamIdsString) -> Unit {
        return {
            retrieveLastGame(request, it, done,
                    retry(request, it, done, fail))
        }
    }

    private fun retry(request: GameSettingsRequest, teamIds: TeamIdsString, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit): (Throwable) -> Unit =
            { createNewGame(request, teamIds, done, fail) }

    private fun ensureTeamPlayersExist(teamNamesInput: TeamNamesString, done: (TeamIdsString) -> Unit, fail: (Throwable) -> Unit) {
        createTeamsUsecase.start(teamNamesInput, done, fail)
    }

    private fun createNewGame(gameSettingsRequest: GameSettingsRequest, teamNames: TeamIdsString, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        createGameUsecase.start(gameSettingsRequest, teamNames, done, fail)
    }

    private fun retrieveLastGame(gameSettingsRequest: GameSettingsRequest, teamIds: TeamIdsString, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        createGameUsecase.fetchLatest(gameSettingsRequest, teamIds, done, fail)
    }
}