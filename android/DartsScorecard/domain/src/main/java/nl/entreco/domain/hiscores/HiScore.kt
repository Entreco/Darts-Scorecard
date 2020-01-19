package nl.entreco.domain.hiscores

data class HiScore(val playerId: Long, val playerName: String, val hiScore: List<HiScoreItem>)