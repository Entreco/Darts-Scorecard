package nl.entreco.domain.play.mastercaller

/**
 * Created by entreco on 14/03/2018.
 */
sealed class Sound (val file: String)
class Fx00 : Sound("00.ogg")
class Fx01 : Sound("01.ogg")