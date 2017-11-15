package nl.entreco.domain.play.repository

import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.model.Game

/**
 * Created by Entreco on 15/11/2017.
 */
interface GameRepository {
    fun new(arbiter: Arbiter) : Game
}