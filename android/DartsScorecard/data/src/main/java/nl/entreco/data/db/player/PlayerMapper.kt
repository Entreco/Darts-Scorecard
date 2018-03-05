package nl.entreco.data.db.player

import nl.entreco.data.db.Mapper
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.PlayerPrefs

/**
 * Created by Entreco on 16/12/2017.
 */
class PlayerMapper : Mapper<PlayerTable, Player> {
    override fun to(from: PlayerTable): Player {
        if (from.name.isEmpty()) throw IllegalStateException("name:'${from.name}' is invalid")
        else if(!(0..21).contains(from.fav.toInt())) throw IllegalStateException("fav: '${from.fav}' is invalid")
        return Player(from.name, from.id, PlayerPrefs(from.fav.toInt()), from.image)
    }
}
