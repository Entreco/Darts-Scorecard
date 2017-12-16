package nl.entreco.dartsscorecard.play.score

import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.usecase.CreateGameInput

/**
 * Created by Entreco on 12/12/2017.
 */
interface GameLoadable {
    fun startWith(game: Game, settings: CreateGameInput, uiCallback: UiCallback)
}