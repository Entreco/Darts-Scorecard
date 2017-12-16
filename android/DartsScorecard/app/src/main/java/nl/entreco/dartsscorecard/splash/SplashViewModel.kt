package nl.entreco.dartsscorecard.splash

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.play.model.players.TeamsString
import nl.entreco.domain.play.usecase.CreateGameInput
import nl.entreco.domain.play.usecase.CreateGameUsecase
import nl.entreco.domain.play.usecase.CreateTeamsUsecase
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class SplashViewModel @Inject constructor(private val createGameUsecase: CreateGameUsecase, private val createTeamsUsecase: CreateTeamsUsecase) : BaseViewModel() {

    fun ensureTeamPlayersExist(teamsInput: TeamsString, callback: CreateTeamsUsecase.Callback) {
        createTeamsUsecase.start(teamsInput, callback)
    }

    fun createNewGame(createGameInput: CreateGameInput, teams: TeamsString, callback: CreateGameUsecase.Callback) {
        createGameUsecase.start(createGameInput, teams, callback)
    }

    fun retrieveLastGame(createGameInput: CreateGameInput, callback: CreateGameUsecase.Callback) {
        createGameUsecase.fetchLatest(createGameInput, callback)
    }
}