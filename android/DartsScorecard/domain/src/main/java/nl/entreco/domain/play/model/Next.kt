package nl.entreco.domain.play.model

import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team

/**
 * Created by Entreco on 20/11/2017.
 */
data class Next(val team: Team, val teamIndex: Int, val player: Player)