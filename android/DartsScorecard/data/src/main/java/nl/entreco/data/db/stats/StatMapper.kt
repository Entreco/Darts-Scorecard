package nl.entreco.data.db.stats

import nl.entreco.data.db.meta.MetaTable
import nl.entreco.data.db.turn.TurnMapper
import nl.entreco.data.db.turn.TurnTable
import nl.entreco.domain.model.LiveStat
import nl.entreco.domain.model.Turn

/**
 * Created by entreco on 16/01/2018.
 */
class StatMapper {

    fun to(turns: List<TurnTable>, metas: List<MetaTable>): Map<Long, LiveStat> {
        val list = HashMap<Long, LiveStat>()
        turns.forEachIndexed { index, turn ->
            var liveStat: LiveStat? = null
            if (list.containsKey(turn.player)) {
                liveStat = list[turn.player]
            }
            list[turn.player] = to(turn, metas[index]) + liveStat
        }
        return list
    }

    fun to(turn: TurnTable, meta: MetaTable): LiveStat {
        val t = TurnMapper().to(turn)
        val n180 = if (t.total() == 180) 1 else 0
        val n140 = if (t.total() in 140..179) 1 else 0
        val n100 = if (t.total() in 100..139) 1 else 0
        val n60 = if (t.total() in 60..99) 1 else 0
        val n20 = if (t.total() in 20..59) 1 else 0
        val checkout = if (didFinish(t, meta)) 1 else 0
        val highCo = if (didFinish(t, meta)) listOf(t.total()) else emptyList()
        val breaks = if (meta.breakMade) 1 else 0
        return LiveStat(turn.player, t.total(), t.dartsUsed(), n180, n140, n100, n60, n20, meta.atCheckout, checkout, breaks, listOf(t.total()), highCo)
    }

    private fun didFinish(t: Turn, meta: MetaTable) = t.total() == meta.score
}