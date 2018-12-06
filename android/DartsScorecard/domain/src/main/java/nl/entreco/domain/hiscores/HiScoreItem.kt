package nl.entreco.domain.hiscores

sealed class HiScoreItem(val value: String){
    data class OverallAverage(private val _value: String) : HiScoreItem(_value)
    data class Num180(private val _value: String) : HiScoreItem(_value)
    data class Num140(private val _value: String) : HiScoreItem(_value)
    data class Num100(private val _value: String) : HiScoreItem(_value)
    data class Num60(private val _value: String) : HiScoreItem(_value)
}



