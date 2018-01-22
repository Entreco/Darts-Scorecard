package nl.entreco.data.db.stats

import nl.entreco.data.db.meta.MetaTable
import nl.entreco.data.db.turn.TurnMapper
import nl.entreco.data.db.turn.TurnTable
import nl.entreco.domain.model.Stat
import nl.entreco.domain.model.Turn

/**
 * Created by entreco on 16/01/2018.
 */
class StatMapper {

    fun to(turns: List<TurnTable>, metas: List<MetaTable>): Map<Long, Stat> {
        val list = HashMap<Long, Stat>()
        turns.forEachIndexed { index, turn ->
            var stat: Stat? = null
            if (list.containsKey(turn.player)) {
                stat = list[turn.player]
            }
            list[turn.player] = to(turn, metas[index]) + stat
        }
        return list
    }

    fun to(turn: TurnTable, meta: MetaTable): Stat {
        val t = TurnMapper().to(turn)
        val n180 = if (t.total() == 180) 1 else 0
        val n140 = if (t.total() in 140..179) 1 else 0
        val n100 = if (t.total() in 100..139) 1 else 0
        val checkout = if (didFinish(t, meta)) 1 else 0
        val highCo = if (didFinish(t, meta)) listOf(t.total()) else listOf(0)
        val breaks = 0 // TODO: How to measure breaks
        return Stat(turn.player, t.total(), t.dartsUsed(), n180, n140, n100, meta.atCheckout, checkout, breaks, listOf(t.total()), highCo)
    }

    private fun didFinish(t: Turn, meta: MetaTable) =
            t.lastIsDouble() && meta.score == 0
}