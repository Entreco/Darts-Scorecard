package nl.entreco.domain.model

import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team

/**
 * Created by Entreco on 20/11/2017.
 */
data class Next(val state: State, val team: Team, val teamIndex: Int, val player: Player, val requiredScore: Score)