package nl.entreco.domain.repository

import nl.entreco.domain.model.Turn

/**
 * Created by Entreco on 23/12/2017.
 */
data class StoreTurnRequest(val gameId: Long, val turn: Turn)