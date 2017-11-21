package nl.entreco.dartsscorecard.base

import android.app.Activity
import android.content.SharedPreferences
import android.support.annotation.StyleRes
import nl.entreco.dartsscorecard.R
import javax.inject.Inject

/**
 * Created by Entreco on 21/11/2017.
 */
class Styler @Inject constructor(private val prefs : SharedPreferences, private val activity: Activity){

    enum class Style(@StyleRes val style: Int) {
        PDC(R.style.Pdc),
        BDO(R.style.Bdo);
    }

    fun get() : Int {
        return prefs.getInt("curStyle", Style.PDC.style)
    }

    fun switch(){
        val curStyle = prefs.getInt("curStyle", Style.PDC.style)
        prefs.edit().putInt("curStyle", swap(curStyle)).apply()
        activity.recreate()
    }

    private fun swap(style: Int) : Int {
        return if(style == R.style.Pdc) Style.BDO.style
        else Style.PDC.style
    }
}