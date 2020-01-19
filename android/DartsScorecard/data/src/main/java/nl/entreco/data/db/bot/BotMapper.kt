package nl.entreco.data.db.bot

import nl.entreco.data.db.Mapper
import nl.entreco.domain.model.players.Bot

class BotMapper : Mapper<BotTable, Bot> {
    override fun to(from: BotTable): Bot {
        require(from.name.isNotEmpty()) { "name:'${from.name}' is invalid" }
        return Bot(from.name, from.level, from.id)
    }
}