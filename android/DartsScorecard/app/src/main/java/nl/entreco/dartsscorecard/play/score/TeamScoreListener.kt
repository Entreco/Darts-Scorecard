package nl.entreco.dartsscorecard.play.score

import nl.entreco.domain.play.listeners.SpecialEventListener
import nl.entreco.domain.play.listeners.events.NineDartEvent
import nl.entreco.domain.play.listeners.events.SpecialEvent


interface TeamScoreListener : SpecialEventListener<SpecialEvent> {
    override fun handle(event: SpecialEvent) {
        when (event) {
            is NineDartEvent -> onNineDartEvent(event)
        }
    }

    fun onNineDartEvent(event: NineDartEvent)
}