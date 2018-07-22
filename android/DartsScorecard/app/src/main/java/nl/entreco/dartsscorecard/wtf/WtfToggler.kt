package nl.entreco.dartsscorecard.wtf

import nl.entreco.domain.wtf.WtfItem

interface WtfToggler {
    fun toggle(item: WtfItem)
}