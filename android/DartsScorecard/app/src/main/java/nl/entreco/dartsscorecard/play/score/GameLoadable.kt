package nl.entreco.dartsscorecard.play.score

import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.usecase.GameSettingsRequest

/**
 * Created by Entreco on 12/12/2017.
 */
interface GameLoadable {
    fun startWith(teams: Array<Team>, settings: GameSettingsRequest, uiCallback: UiCallback)
}