package nl.entreco.domain.repository

import nl.entreco.domain.model.Turn

/**
 * Created by Entreco on 23/12/2017.
 */
interface TurnRepository {
    fun fetchTurnsForGame(gameId: Long): List<Turn>
    fun store(gameId: Long, turn: Turn)
}