package nl.entreco.dartsscorecard.splash

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.play.model.players.TeamIdsString
import nl.entreco.domain.play.model.players.TeamNamesString
import nl.entreco.domain.play.usecase.GameSettingsRequest
import nl.entreco.domain.play.usecase.CreateGameUsecase
import nl.entreco.domain.play.usecase.CreateTeamsUsecase
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class SplashViewModel @Inject constructor(private val createGameUsecase: CreateGameUsecase, private val createTeamsUsecase: CreateTeamsUsecase) : BaseViewModel() {

    fun ensureTeamPlayersExist(teamNamesInput: TeamNamesString, callback: CreateTeamsUsecase.Callback) {
        createTeamsUsecase.start(teamNamesInput, callback)
    }

    fun createNewGame(gameSettingsRequest: GameSettingsRequest, teamNames: TeamIdsString, callback: CreateGameUsecase.Callback) {
        createGameUsecase.start(gameSettingsRequest, teamNames, callback)
    }

    fun retrieveLastGame(gameSettingsRequest: GameSettingsRequest, teamIds: TeamIdsString, callback: CreateGameUsecase.Callback) {
        createGameUsecase.fetchLatest(gameSettingsRequest, teamIds,  callback)
    }
}