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

    enum class Style(val style: String) {
        PDC_2018("Pdc_2018"),
        BDO_2018("Bdo_2018"),
        BDO("Bdo"),
        PDC("Pdc");
    }

    fun get(): Int {
        return when(prefs.getString("curStyle", Style.PDC_2018.style)){
            Style.PDC_2018.style -> R.style.Pdc_2018
            Style.BDO_2018.style -> R.style.Bdo_2018
            Style.PDC.style -> R.style.Pdc
            Style.BDO.style -> R.style.Bdo
            else -> R.style.Pdc_2018
        }
    }

    fun switch() {
        prefs.edit().putString("curStyle", swap(get())).apply()
        activity.recreate()
    }

    private fun swap(style: Int): String {
        return when (style) {
            R.style.Pdc_2018 -> Style.BDO_2018.style
            R.style.Bdo_2018 -> Style.PDC.style
            R.style.Pdc -> Style.BDO.style
            else -> Style.PDC_2018.style
        }
    }
}