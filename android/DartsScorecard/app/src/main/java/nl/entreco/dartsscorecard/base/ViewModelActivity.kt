package nl.entreco.dartsscorecard.base

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelModule

/**
 * Created by Entreco on 14/11/2017.
 */
abstract class ViewModelActivity : AppCompatActivity() {

    private val styler by lazy { Styler(PreferenceManager.getDefaultSharedPreferences(this), this) }

    val Activity.app: App
        get() = application as App

    protected val component by lazy { app.appComponent.plus(ViewModelModule(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(styler.get())
        super.onCreate(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified VM : ViewModel> ViewModelActivity.viewModelProvider(
            mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
            crossinline provider: () -> VM) = lazy(mode) {
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T1 : ViewModel> create(aClass: Class<T1>) =
                    provider() as T1
        }).get(VM::class.java)
    }

    protected fun swapStyle() {
        styler.switch()
    }
}