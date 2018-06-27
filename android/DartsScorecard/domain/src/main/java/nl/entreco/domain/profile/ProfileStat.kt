package nl.entreco.domain.profile


data class ProfileStat(val playerIds: Long, val numberOfGames: Int, val numberOfWins: Int,
                       val numberOfDarts: Int, val numberOfPoints: Int,
                       val numberOfDarts9: Int, val numberOfPoints9: Int,
                       val numberOf180s: Int, val numberOf140s: Int, val numberOf100s: Int,
                       val numberOf60s: Int, val numberOf20s: Int, val numberOf0s: Int,
                       val numberOfDartsAtFinish: Int, val numberOfFinishes: Int)