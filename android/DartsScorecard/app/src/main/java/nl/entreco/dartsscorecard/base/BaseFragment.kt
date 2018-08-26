package nl.entreco.dartsscorecard.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.annotation.StringRes
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelComponent
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelModule

abstract class BaseFragment : Fragment() {

    inline fun <reified VM> componentProvider(
            mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
            crossinline provider: (ViewModelComponent) -> VM) = lazy(mode) {

        val app = activity!!.application as App
        val component = app.appComponent.plus(ViewModelModule(activity!!))
        provider(component)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified VM : ViewModel> viewModelProvider(
            mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
            crossinline provider: () -> VM) = lazy(mode) {
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T1 : ViewModel> create(aClass: Class<T1>) =
                    provider() as T1
        }).get(VM::class.java)
    }

    fun showSnackbarMessage(@StringRes resId: Int, @BaseTransientBottomBar.Duration duration: Int) {
        view?.let {
            val snackbar = Snackbar.make(it, resId, duration)
//            val layout = snackbar.view as Snackbar.SnackbarLayout
//            layout.setBackgroundColor(context.getColorCompat(R.color.transparent_black))
            snackbar.show()
        }
    }
}