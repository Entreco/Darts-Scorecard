package nl.entreco.domain.play.usecase

import java.util.*

/**
 * Created by Entreco on 16/12/2017.
 */
data class CreateTeamsInput(val team: Array<Array<String>>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CreateTeamsInput

        if (!Arrays.equals(team, other.team)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(team)
    }
}