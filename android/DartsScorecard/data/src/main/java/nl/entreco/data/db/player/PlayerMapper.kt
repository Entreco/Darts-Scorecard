package nl.entreco.data.db.player

import nl.entreco.data.db.Mapper
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.PlayerPrefs

/**
 * Created by Entreco on 16/12/2017.
 */
class PlayerMapper : Mapper<PlayerTable, Player> {
    override fun to(from: PlayerTable): Player {
        if (from.name.isEmpty()) throw IllegalStateException("name:'${from.name}' is invalid")
        return Player(from.name, PlayerPrefs(from.fav.toInt()))
    }
}