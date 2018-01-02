package nl.entreco.dartsscorecard.base

import android.app.Activity
import android.content.SharedPreferences
import android.support.annotation.StyleRes
import nl.entreco.dartsscorecard.R
import javax.inject.Inject

/**
 * Created by Entreco on 21/11/2017.
 */
class Styler @Inject constructor(private val prefs: SharedPreferences, private val activity: Activity) {

    enum class Style(@StyleRes val style: Int) {
        PDC_2018(R.style.Pdc_2018),
        BDO(R.style.Bdo),
        PDC(R.style.Pdc);
    }

    fun get(): Int {
        return prefs.getInt("curStyle", Style.PDC.style)
    }

    fun switch() {
        val curStyle = prefs.getInt("curStyle", Style.PDC.style)
        prefs.edit().putInt("curStyle", swap(curStyle)).apply()
        activity.recreate()
    }

    private fun swap(style: Int): Int {
        return when (style) {
            R.style.Pdc_2018 -> Style.BDO.style
            R.style.Bdo -> Style.PDC.style
            else -> Style.PDC_2018.style
        }
    }
}