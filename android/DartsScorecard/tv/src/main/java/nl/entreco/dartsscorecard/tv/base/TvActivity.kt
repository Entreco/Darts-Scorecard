package nl.entreco.dartsscorecard.tv.base

import android.support.v4.app.FragmentActivity

abstract class TvActivity : FragmentActivity() {

//    @Suppress("UNCHECKED_CAST")
//    inline fun <reified VM : ViewModel> viewModelProvider(
//            mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
//            crossinline provider: () -> VM) = lazy(mode) {
//        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
//            override fun <T1 : ViewModel> create(aClass: Class<T1>) =
//                    provider() as T1
//        }).get(VM::class.java)
//    }
//
//    inline fun <reified VM> componentProvider(
//            mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
//            crossinline provider: (TvViewModelComponent) -> VM) = lazy(mode) {
//        val component = app.tvComponent.plus(TvViewModelModule(this))
//        provider(component)
//    }
}