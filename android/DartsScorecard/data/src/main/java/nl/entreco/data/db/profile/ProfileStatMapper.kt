package nl.entreco.data.db.profile

import nl.entreco.domain.profile.ProfileStat


class ProfileStatMapper {

    fun to(playerId: Long, stats: List<ProfileTable>): ProfileStat {
        return ProfileStat(playerId, stats.size, stats.sumBy {  it.numDarts }, stats.sumBy { it.totalScore })
    }
}