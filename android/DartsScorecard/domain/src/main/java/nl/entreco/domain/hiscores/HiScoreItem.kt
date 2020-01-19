package nl.entreco.domain.hiscores

sealed class HiScoreItem(val value: Float, val display: String){
    data class OverallAvg(private val _value: Float) : HiScoreItem(_value, "%.2f".format(_value))
    data class ScoringAvg(private val _value: Float) : HiScoreItem(_value, "%.2f".format(_value))
    data class CheckoutPerc(private val _value: Float) : HiScoreItem(_value, "%.2f".format(_value) + "%")
    data class WinRatio(private val _value: Float, val played:Int, val won:Int) : HiScoreItem(_value, "$won/$played".format(_value))
    data class Num180(private val _value: Int) : HiScoreItem(_value.toFloat(), _value.toString())
    data class Num140(private val _value: Int) : HiScoreItem(_value.toFloat(), _value.toString())
    data class Num100(private val _value: Int) : HiScoreItem(_value.toFloat(), _value.toString())
    data class Num60(private val _value: Int) : HiScoreItem(_value.toFloat(), _value.toString())
    data class Num20(private val _value: Int) : HiScoreItem(_value.toFloat(), _value.toString())
    data class NumBust(private val _value: Int) : HiScoreItem(_value.toFloat(), _value.toString())
    data class BestMatchAvg(private val _value: Float) : HiScoreItem(_value, "%.2f".format(_value))
    data class BestMatchCheckout(private val _value: Float) : HiScoreItem(_value, "%.2f".format(_value))
    data class HighestScore(private val _value: Float) : HiScoreItem(_value, "%.2f".format(_value))
    data class HighestCheckout(private val _value: Float) : HiScoreItem(_value, "%.2f".format(_value))
}



