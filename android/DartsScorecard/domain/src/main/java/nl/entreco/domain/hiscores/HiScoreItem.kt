package nl.entreco.domain.hiscores

sealed class HiScoreItem{
    abstract fun value() : String
    abstract fun sort() : Float
    data class OverallAverage(private val value: Float) : HiScoreItem(){
        override fun value() = "%.2f".format(value)
        override fun sort(): Float = value
    }
    data class Num180(val value: Int) : HiScoreItem(){
        override fun value() = value.toString()
        override fun sort(): Float = value.toFloat()
    }
    data class Num140(val value: Int) : HiScoreItem(){
        override fun value() = value.toString()
        override fun sort(): Float = value.toFloat()
    }
    data class Num100(val value: Int) : HiScoreItem(){
        override fun value() = value.toString()
        override fun sort(): Float = value.toFloat()
    }
    data class Num60(val value: Int) : HiScoreItem(){
        override fun value() = value.toString()
        override fun sort(): Float = value.toFloat()
    }
}



