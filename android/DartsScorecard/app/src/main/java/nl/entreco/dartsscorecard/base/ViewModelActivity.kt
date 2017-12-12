package nl.entreco.dartsscorecard.base

import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelComponent
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelModule

/**
 * Created by Entreco on 14/11/2017.
 */
abstract class ViewModelActivity : AppCompatActivity() {

    private val styler by lazy { Styler(PreferenceManager.getDefaultSharedPreferences(this), this) }

    val Activity.app: App
        get() = application as App

    private val component by lazy { app.appComponent.plus(ViewModelModule(this)) }

    abstract fun inject(component: ViewModelComponent)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(styler.get())
        inject(component)
        super.onCreate(savedInstanceState)
    }

    protected fun swapStyle() {
        styler.switch()
    }
}