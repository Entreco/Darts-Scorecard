package nl.entreco.dartsscorecard.faq

import nl.entreco.domain.wtf.WtfItem

interface WtfToggler {
    fun toggle(item: WtfItem)
}