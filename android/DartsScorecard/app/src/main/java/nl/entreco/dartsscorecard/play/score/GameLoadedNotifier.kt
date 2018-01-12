package nl.entreco.dartsscorecard.play.score

import nl.entreco.domain.model.Score
import nl.entreco.domain.model.players.Team

/**
 * Created by Entreco on 12/12/2017.
 */
interface GameLoadedNotifier<in T> {
    fun onLoaded(teams: Array<Team>, scores: Array<Score>, info: T, uiCallback: UiCallback?)
}