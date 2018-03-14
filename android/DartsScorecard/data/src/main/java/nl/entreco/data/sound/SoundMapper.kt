package nl.entreco.data.sound

import android.support.annotation.RawRes
import nl.entreco.data.R
import nl.entreco.domain.play.mastercaller.Fx00
import nl.entreco.domain.play.mastercaller.Fx01
import nl.entreco.domain.play.mastercaller.None
import nl.entreco.domain.play.mastercaller.Sound

/**
 * Created by entreco on 14/03/2018.
 */
class SoundMapper {

    @RawRes
    fun toRaw(sound: Sound): Int {
        return when (sound) {
            is None -> 0
            is Fx00 -> R.raw.dsc_pro0
            is Fx01 -> R.raw.dsc_pro1
            else -> throw IllegalStateException("unknown sound $sound")
        }
    }
}