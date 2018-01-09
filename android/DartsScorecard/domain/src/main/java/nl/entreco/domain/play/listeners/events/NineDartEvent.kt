package nl.entreco.domain.play.listeners.events

import nl.entreco.domain.model.players.Player


class NineDartEvent(private val possible: Boolean, private val by: Player) : SpecialEvent {
    fun isPossible(): Boolean {
        return possible
    }

    fun by(): Player {
        return by
    }
}