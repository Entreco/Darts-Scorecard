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

    companion object {
        private const val CURRENT_STYLE = "currentStyle"
    }

    enum class Style(val style: String) {
        PDC_2018("Pdc_2018"),
        BDO_2018("Bdo_2018"),
        BDO("Bdo"),
        PDC("Pdc"),
        PCF("Pcf");
    }

    fun get(): Int {
        return when(prefs.getString(CURRENT_STYLE, Style.PDC_2018.style)){
            Style.PDC_2018.style -> R.style.Pdc_2018
            Style.BDO_2018.style -> R.style.Bdo_2018
            Style.PDC.style -> R.style.Pdc
            Style.BDO.style -> R.style.Bdo
            Style.PCF.style -> R.style.Pcf
            else -> R.style.Pdc_2018
        }
    }

    fun switch() {
        prefs.edit().putString(CURRENT_STYLE, swap(get())).apply()
        activity.recreate()
    }

    private fun swap(style: Int): String {
        return when (style) {
            R.style.Pdc_2018 -> Style.BDO_2018.style
            R.style.Bdo_2018 -> Style.PCF.style
            R.style.Pcf -> Style.PDC.style
            R.style.Pdc -> Style.BDO.style
            else -> Style.PDC_2018.style
        }
    }
}