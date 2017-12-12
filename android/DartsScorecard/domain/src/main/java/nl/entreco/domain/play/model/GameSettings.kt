package nl.entreco.domain.play.model

import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.settings.ScoreSettings

/**
 * Created by Entreco on 12/12/2017.
 */
data class GameSettings(val uid: String, val arbiter: Arbiter, val initialScore: Score, val settings: ScoreSettings, val teams: Array<Team>)