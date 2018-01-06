package nl.entreco.domain.play.start

import nl.entreco.domain.model.Turn
import java.util.*

/**
 * Created by entreco on 06/01/2018.
 */
data class RetrieveTurnsResponse(val turns: Array<Turn>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RetrieveTurnsResponse

        if (!Arrays.equals(turns, other.turns)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(turns)
    }
}