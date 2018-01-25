package nl.entreco.domain.play.start

import nl.entreco.domain.model.Turn

/**
 * Created by entreco on 06/01/2018.
 */
data class RetrieveTurnsResponse(val turns: List<Pair<Long, Turn>>)