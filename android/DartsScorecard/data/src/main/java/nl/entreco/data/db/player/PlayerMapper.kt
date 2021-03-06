package nl.entreco.data.db.player

import nl.entreco.data.db.Mapper
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.PlayerPrefs

/**
 * Created by Entreco on 16/12/2017.
 */
class PlayerMapper : Mapper<PlayerTable, Player> {
    override fun to(from: PlayerTable): Player {
        require(from.name.isNotEmpty()) { "name:'${from.name}' is invalid" }
        require((0..21).contains(from.fav.toInt())) { "fav: '${from.fav}' is invalid" }

        return Player(from.name, from.id, PlayerPrefs(from.fav.toInt()), from.image)
    }
}
