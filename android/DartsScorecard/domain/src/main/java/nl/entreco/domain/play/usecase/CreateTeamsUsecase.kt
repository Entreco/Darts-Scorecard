package nl.entreco.domain.play.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.model.players.TeamsString
import nl.entreco.domain.play.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by Entreco on 16/12/2017.
 */
class CreateTeamsUsecase @Inject constructor(private val playerRepository: PlayerRepository, private val bg: Background, private val fg: Foreground) {
    interface Callback {
        fun onTeamsCreated(teams: TeamsString)
        fun onTeamsFailed(err: Throwable)
    }

    fun start(teamsInput: TeamsString, callback: CreateTeamsUsecase.Callback) {
        bg.post(Runnable {

            try {

                teamsInput.asPlayersList().forEach {
                    val player = playerRepository.fetchByName(it.name)
                    if(player == null) {
                        playerRepository.create(it.name, it.prefs.favoriteDouble)
                    }
                }

                fg.post(Runnable {
                    callback.onTeamsCreated(teamsInput)
                })
            } catch(oops: Exception){
                fg.post(Runnable { callback.onTeamsFailed(oops) })
            }
        })
    }
}