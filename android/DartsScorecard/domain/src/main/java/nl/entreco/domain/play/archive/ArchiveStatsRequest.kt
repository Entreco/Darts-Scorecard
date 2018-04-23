package nl.entreco.domain.play.archive

import nl.entreco.domain.model.LiveStat


class ArchiveStatsRequest(val gameId: Long, val teamIds: String, val stats: Map<Long, List<LiveStat>>)