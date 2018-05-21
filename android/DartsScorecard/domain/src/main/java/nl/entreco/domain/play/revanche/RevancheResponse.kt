package nl.entreco.domain.play.revanche

import nl.entreco.domain.model.Game
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.settings.ScoreSettings

/**
 * Created by entreco on 19/02/2018.
 */
class RevancheResponse(val game: Game, val settings: ScoreSettings, val teams: Array<Team>, val teamIds: String)
