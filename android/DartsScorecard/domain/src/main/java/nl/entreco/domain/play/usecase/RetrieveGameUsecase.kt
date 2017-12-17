package nl.entreco.domain.play.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.repository.GameRepository
import javax.inject.Inject

/**
 * Created by Entreco on 14/11/2017.
 */
class RetrieveGameUsecase @Inject constructor(private val gameRepository: GameRepository,
                                              private val bg: Background,
                                              private val fg: Foreground) {

    fun start(gameId: Long, teams: Array<Team>, ok: (Game) -> Unit, err: (Throwable) -> Unit) {
        bg.post(Runnable {

            try {
                val game = gameRepository.fetchBy(gameId)
                game.setTeams(teams)

                fg.post(Runnable {
                    ok(game)
                })
            } catch (ohno: Exception) {
                fg.post(Runnable { err(ohno) })
            }
        })
    }
}