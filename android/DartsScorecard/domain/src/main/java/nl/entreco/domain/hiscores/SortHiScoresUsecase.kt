package nl.entreco.domain.hiscores

import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import javax.inject.Inject

class SortHiScoresUsecase @Inject constructor(
    bg: Background, fg: Foreground,
) : BaseUsecase(bg, fg) {

    fun go(
        request: SortHiScoresRequest,
        done: (List<SortHiScoresResponse>) -> Unit,
        fail: (Throwable) -> Unit,
    ) {
        onBackground({
            val sorted = sortByHiScore(request.items)
            val rankings = standardRanking(sorted)
            val ranked = sorted.mapIndexed { index, item ->
                item.copy(pos = rankings[index])
            }
            onUi { done(ranked) }
        }, fail)
    }

    private fun sortByHiScore(items: Map<HiScore, HiScoreItem>): List<SortHiScoresResponse> {
        return items.toList().sortedByDescending { it.second.value }.map {
            SortHiScoresResponse(it.first.playerId, it.first.playerName, it.second.display, it.second.value, 0)
        }
    }

    private fun standardRanking(scores: List<SortHiScoresResponse>): IntArray {
        val rankings = IntArray(scores.size)
        rankings[0] = 1
        for (i in 1 until scores.size) rankings[i] = if (scores[i].value == scores[i - 1].value) rankings[i - 1] else i + 1
        return rankings
    }
}