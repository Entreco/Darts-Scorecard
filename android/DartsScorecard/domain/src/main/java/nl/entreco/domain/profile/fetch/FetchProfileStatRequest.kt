package nl.entreco.domain.profile.fetch

import java.util.*


data class FetchProfileStatRequest(val playerIds: LongArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FetchProfileStatRequest

        if (!Arrays.equals(playerIds, other.playerIds)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(playerIds)
    }
}