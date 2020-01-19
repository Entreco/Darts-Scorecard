package nl.entreco.dartsscorecard.play.live

import androidx.databinding.ObservableField

class TeamSetStat(private val set:Int, setAvg: String, setCheckout: String, setCheckoutPerc: String) {
    val avg = ObservableField(setAvg)
    val checkOut = ObservableField(setCheckout)
    val checkOutPerc = ObservableField(setCheckoutPerc)
}