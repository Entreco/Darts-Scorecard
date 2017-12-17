package nl.entreco.domain.play.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.repository.GameRepository
import nl.entreco.domain.play.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by Entreco on 14/11/2017.
 */
class RetrieveGameUsecase @Inject constructor(private val gameRepository: GameRepository,
                                              private val playerRepository: PlayerRepository,
                                              private val bg: Background,
                                              private val fg: Foreground) {

    fun start(settings: RetrieveGameRequest, ok: (Game) -> Unit, err: (Throwable) -> Unit) {
        bg.post(Runnable {

            try {
                val teams = playerRepository.fetchTeams(settings.teamIds.toString())
                val game = gameRepository.fetchBy(settings.gameId)
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