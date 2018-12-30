package nl.entreco.dartsscorecard.play.live

import androidx.databinding.ObservableField

class TeamSetStat(private val set:Int, setAvg: String, setCheckout: String, setCheckoutPerc: String) {
    val avg = ObservableField<String>(setAvg)
    val checkOut = ObservableField<String>(setCheckout)
    val checkOutPerc = ObservableField<String>(setCheckoutPerc)
}