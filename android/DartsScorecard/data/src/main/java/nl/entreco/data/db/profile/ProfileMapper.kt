package nl.entreco.data.db.profile

import nl.entreco.data.db.Mapper
import nl.entreco.data.db.player.PlayerTable
import nl.entreco.domain.model.players.PlayerPrefs
import nl.entreco.domain.profile.Profile

/**
 * Created by entreco on 23/02/2018.
 */
class ProfileMapper : Mapper<PlayerTable, Profile> {
    override fun to(from: PlayerTable): Profile {
        if (from.name.isEmpty()) throw IllegalStateException("name:'${from.name}' is invalid")
        return Profile(from.name, from.id, from.image, PlayerPrefs(from.fav.toInt()))
    }
}
