package nl.entreco.data.db.profile

import nl.entreco.domain.profile.ProfileStat


class ProfileStatMapper {

    fun to(playerId: Long, stats: List<ProfileTable>): ProfileStat {
        return ProfileStat(playerId, stats.size,
                stats.count { it.didWin },
                stats.sumBy { it.numDarts },
                stats.sumBy { it.totalScore },
                stats.sumBy { it.numDarts9 },
                stats.sumBy { it.totalScore9 },
                stats.sumBy { it.num180s },
                stats.sumBy { it.num140s },
                stats.sumBy { it.num100s },
                stats.sumBy { it.num60s },
                stats.sumBy { it.num20s },
                stats.sumBy { it.num0s },
                stats.sumBy { it.numDartsAtFinish },
                stats.sumBy { it.numFinishes })
    }
}