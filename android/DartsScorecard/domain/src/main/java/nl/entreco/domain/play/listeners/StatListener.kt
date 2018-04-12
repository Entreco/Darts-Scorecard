package nl.entreco.domain.play.listeners

/**
 * Created by entreco on 22/01/2018.
 */
interface StatListener {
    fun onStatsChange(turnId: Long, metaId: Long)
    fun onGameFinished(gameId: Long)
}