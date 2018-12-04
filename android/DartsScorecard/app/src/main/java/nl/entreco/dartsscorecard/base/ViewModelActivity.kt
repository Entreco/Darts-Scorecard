package nl.entreco.dartsscorecard.base

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelComponent
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelModule
import nl.entreco.dartsscorecard.faq.WtfActivity

/**
 * Created by Entreco on 14/11/2017.
 */
abstract class ViewModelActivity : AppCompatActivity() {

    private val styler by lazy { Styler(PreferenceManager.getDefaultSharedPreferences(this), this) }

    val Activity.app: App
        get() = application as App


    @Suppress("UNCHECKED_CAST")
    inline fun <reified VM : ViewModel> viewModelProvider(
            mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
            crossinline provider: () -> VM) = lazy(mode) {
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T1 : ViewModel> create(aClass: Class<T1>) =
                    provider() as T1
        }).get(VM::class.java)
    }

    inline fun <reified VM> componentProvider(
            mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
            crossinline provider: (ViewModelComponent) -> VM) = lazy(mode) {
        val component = app.appComponent.plus(ViewModelModule(this))
        provider(component)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(styler.get())
        super.onCreate(savedInstanceState)
    }

    override fun onRestart() {
        setTheme(styler.get())
        super.onRestart()
    }

    protected fun swapStyle() {
        styler.switch()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_faq -> {
                WtfActivity.launch(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected fun initToolbar(toolbar: Toolbar, @StringRes title: Int = R.string.app_name, showHomeEnabled: Boolean = true) {
        setSupportActionBar(toolbar)
        setTitle(title)
        supportActionBar?.setDisplayHomeAsUpEnabled(showHomeEnabled)
    }

    protected fun initToolbar(toolbar: Toolbar, title: String, showHomeEnabled: Boolean = true) {
        setSupportActionBar(toolbar)
        setTitle(title)
        supportActionBar?.setDisplayHomeAsUpEnabled(showHomeEnabled)
    }
}