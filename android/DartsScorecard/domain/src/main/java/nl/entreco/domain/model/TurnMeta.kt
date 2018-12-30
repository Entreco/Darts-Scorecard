package nl.entreco.domain.model

/**
 * Created by entreco on 16/01/2018.
 */
data class TurnMeta(val playerId: Long, val turnNumber: Int = 0, val setNumber: Int = 0, val legNumber: Int = 0, val score: Score, val breakMade: Boolean = false)