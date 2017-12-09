package nl.entreco.domain.play.listeners

import nl.entreco.domain.play.model.Next

/**
 * Created by Entreco on 20/11/2017.
 */
interface PlayerListener {
    fun onNext(next: Next)
}