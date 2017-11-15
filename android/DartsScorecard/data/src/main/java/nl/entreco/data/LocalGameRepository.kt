package nl.entreco.data

import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.repository.GameRepository

/**
 * Created by Entreco on 15/11/2017.
 */
class LocalGameRepository : GameRepository {
    override fun new(arbiter: Arbiter) : Game {
        return Game(arbiter)
    }
}