package nl.entreco.domain.profile


data class ProfileStat(val playerId: Long, val numberOfGames: Int, val numberOfWins: Int,
                       val numberOfDarts: Int, val numberOfPoints: Int,
                       val numberOf180s: Int, val numberOf140s: Int, val numberOf100s: Int,
                       val numberOf60s: Int, val numberOf20s: Int, val numberOf0s: Int)