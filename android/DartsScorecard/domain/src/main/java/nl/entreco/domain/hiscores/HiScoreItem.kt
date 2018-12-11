package nl.entreco.domain.hiscores

sealed class HiScoreItem(val value: Float, val display: String){
    data class OverallAverage(private val _value: Float) : HiScoreItem(_value, "%.2f".format(_value))
    data class FirstNineAvg(private val _value: Float) : HiScoreItem(_value, "%.2f".format(_value))
    data class Num180(val _value: Int) : HiScoreItem(_value.toFloat(), _value.toString())
    data class Num140(val _value: Int) : HiScoreItem(_value.toFloat(), _value.toString())
    data class Num100(val _value: Int) : HiScoreItem(_value.toFloat(), _value.toString())
    data class Num60(val _value: Int) : HiScoreItem(_value.toFloat(), _value.toString())
    data class Num20(val _value: Int) : HiScoreItem(_value.toFloat(), _value.toString())
    data class NumBust(val _value: Int) : HiScoreItem(_value.toFloat(), _value.toString())
}



