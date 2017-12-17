package nl.entreco.domain.splash

import nl.entreco.domain.play.model.players.TeamNamesString
import nl.entreco.domain.play.usecase.CreateGameUsecase
import nl.entreco.domain.play.usecase.CreateTeamsUsecase
import nl.entreco.domain.play.usecase.GameSettingsRequest
import nl.entreco.domain.play.usecase.RetrieveGameRequest
import javax.inject.Inject

/**
 * Created by Entreco on 17/12/2017.
 */
class SplashUsecase @Inject constructor(private val createTeamsUsecase: CreateTeamsUsecase, private val createGameUsecase: CreateGameUsecase) {

    fun justDoIt(teamNamesInput: TeamNamesString, req: GameSettingsRequest, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        // TODO: Unify CreateTeams & CreateGame
    }
}