package nl.entreco.domain.profile


data class ProfileStat(val playerId: Long, val numberOfGames: Int, val numberOfWins: Int,
                       val numberOfDarts: Int, val numberOfPoints: Int, val numberOf180s: Int)