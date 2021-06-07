package nl.entreco.data.db.profile

import nl.entreco.domain.profile.ProfileStat


class ProfileStatMapper {

    fun to(stats: List<ProfileTable>): ProfileStat {
        return ProfileStat(stats.size,
            stats.count { it.didWin },
            stats.sumOf { it.numDarts },
            stats.sumOf { it.totalScore },
            stats.sumOf { it.numDarts9 },
            stats.sumOf { it.totalScore9 },
            stats.sumOf { it.num180s },
            stats.sumOf { it.num140s },
            stats.sumOf { it.num100s },
            stats.sumOf { it.num60s },
            stats.sumOf { it.num20s },
            stats.sumOf { it.num0s },
            stats.sumOf { it.numDartsAtFinish },
            stats.sumOf { it.numFinishes })
    }
}