package nl.entreco.dartsscorecard.launch

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.TeamIdsString
import nl.entreco.domain.launch.TeamNamesString
import nl.entreco.domain.launch.usecase.CreateGameUsecase
import nl.entreco.domain.launch.usecase.CreateTeamsUsecase
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class LaunchViewModel @Inject constructor(private val createGameUsecase: CreateGameUsecase, private val createTeamsUsecase: CreateTeamsUsecase) : BaseViewModel() {


    fun createFrom(teams: TeamNamesString, requestCreate: CreateGameRequest, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        ensureTeamPlayersExist(teams,
                handleTeamRetrieved(requestCreate, done, fail),
                fail)
    }

    private fun handleTeamRetrieved(requestCreate: CreateGameRequest, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit): (TeamIdsString) -> Unit {
        return {
            retrieveLastGame(requestCreate, it, done,
                    retry(requestCreate, it, done, fail))
        }
    }

    private fun retry(requestCreate: CreateGameRequest, teamIds: TeamIdsString, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit): (Throwable) -> Unit =
            { createNewGame(requestCreate, teamIds, done, fail) }

    private fun ensureTeamPlayersExist(teamNamesInput: TeamNamesString, done: (TeamIdsString) -> Unit, fail: (Throwable) -> Unit) {
        createTeamsUsecase.start(teamNamesInput, done, fail)
    }

    private fun createNewGame(createGameRequest: CreateGameRequest, teamNames: TeamIdsString, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        createGameUsecase.start(createGameRequest, teamNames, done, fail)
    }

    private fun retrieveLastGame(createGameRequest: CreateGameRequest, teamIds: TeamIdsString, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        createGameUsecase.fetchLatest(createGameRequest, teamIds, done, fail)
    }
}