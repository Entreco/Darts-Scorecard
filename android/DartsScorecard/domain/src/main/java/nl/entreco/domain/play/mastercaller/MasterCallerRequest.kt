package nl.entreco.domain.play.mastercaller

/**
 * Created by entreco on 14/03/2018.
 */
data class MasterCallerRequest(val scored: Int){
    fun toSound() : Sound {
        return when(scored){
            0 -> Fx00()
            1 -> Fx01()
            else -> None()
        }
    }
}