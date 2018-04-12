package nl.entreco.domain.play.revanche

import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.start.Play01Request

/**
 * Created by entreco on 19/02/2018.
 */
class RevancheRequest(val originalRequest: Play01Request, val teams: Array<Team>, val newStartIndex: Int)
