package nl.entreco.dartsscorecard

import android.app.Application
import android.os.StrictMode
import nl.entreco.dartsscorecard.di.application.AppComponent
import nl.entreco.dartsscorecard.di.application.AppModule
import nl.entreco.dartsscorecard.di.application.DaggerAppComponent


/**
 * Created by Entreco on 14/11/2017.
 */
class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
        initStrictMode()
    }

    private fun initDagger() {
        appComponent.inject(this)
    }

    private fun initStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
        }
    }
}
