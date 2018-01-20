package nl.entreco.data.db.stats

import nl.entreco.data.db.meta.MetaTable
import nl.entreco.data.db.turn.TurnMapper
import nl.entreco.data.db.turn.TurnTable
import nl.entreco.domain.model.Stat

/**
 * Created by entreco on 16/01/2018.
 */
class StatMapper {

    fun to(turns: List<TurnTable>, metas: List<MetaTable>): Map<Long, Stat> {
        val list = HashMap<Long, Stat>()
        turns.forEachIndexed { index, turn ->
            var stat : Stat? = null
            if(list.containsKey(turn.player)){
                stat = list[turn.player]
            }
            val updated = to(turn, metas[index]) + stat

            list[turn.player] = updated
        }
        return list
    }

    fun to(turn: TurnTable, meta: MetaTable): Stat {
        val t = TurnMapper().to(turn)
        val avg  = t.total() / t.dartsUsed().toDouble() * 3
        val n180 = if(t.total() == 180) 1 else 0
        val n140 = if(t.total() in 140..179) 1 else 0
        val n100 = if(t.total() in 100..139) 1 else 0
        return Stat(avg, n180, n140, n100)
    }
}