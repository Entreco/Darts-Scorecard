package nl.entreco.domain.play.stats

import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.domain.repository.GameRepository
import nl.entreco.domain.repository.MetaRepository
import nl.entreco.domain.repository.TurnRepository
import javax.inject.Inject

/**
 * Created by entreco on 25/01/2018.
 */
class UndoTurnUsecase @Inject constructor(private val turnRepository: TurnRepository,
                                          private val metaRepository: MetaRepository,
                                          private val gameRepository: GameRepository,
                                          bg: nl.entreco.libcore.threading.Background, fg: nl.entreco.libcore.threading.Foreground
) : nl.entreco.libcore.BaseUsecase(bg, fg) {

    fun exec(req: UndoTurnRequest, done: (UndoTurnResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            /* !Important -> Make sure game does not hang in Finished state! */
            gameRepository.undoFinish(req.gameId)

            val turnsDeleted = turnRepository.undo(req.gameId)
            val metasDeleted = metaRepository.undo(req.gameId)

            onUi { done(UndoTurnResponse(turnsDeleted, metasDeleted)) }
        }, fail)
    }
}