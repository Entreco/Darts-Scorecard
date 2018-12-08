package nl.entreco.domain.hiscores

data class HiScore(val playerId: Long, val playerName: String, val hiScores: List<HiScoreItem>)