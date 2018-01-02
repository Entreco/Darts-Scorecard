package nl.entreco.dartsscorecard.play.score

import nl.entreco.domain.model.Score
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.repository.CreateGameRequest

/**
 * Created by Entreco on 12/12/2017.
 */
interface GameLoadable {
    fun startWith(teams: Array<Team>, scores: Array<Score>, create: CreateGameRequest, uiCallback: UiCallback)
}