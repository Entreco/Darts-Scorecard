package nl.entreco.dartsscorecard.play.input

import nl.entreco.domain.play.listeners.SpecialEventListener
import nl.entreco.domain.play.listeners.events.BustEvent
import nl.entreco.domain.play.listeners.events.NoScoreEvent
import nl.entreco.domain.play.listeners.events.SpecialEvent

/**
 * Created by Entreco on 06/12/2017.
 */
interface InputEventsListener : SpecialEventListener<SpecialEvent> {
    
    override fun handle(event: SpecialEvent) {
        when (event) {
            is BustEvent -> onBustEvent(event)
            is NoScoreEvent -> onNoScoreEvent(event)
        }
    }

    fun onNoScoreEvent(event: NoScoreEvent)

    fun onBustEvent(event: BustEvent)
}