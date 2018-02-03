package nl.entreco.dartsscorecard

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.FirebaseDatabase
import nl.entreco.dartsscorecard.di.application.AppComponent
import nl.entreco.dartsscorecard.di.application.AppModule
import nl.entreco.dartsscorecard.di.application.DaggerAppComponent

/**
 * Created by Entreco on 14/11/2017.
 */
class App : Application() {

    val appComponent: AppComponent by lazy { DaggerAppComponent.builder().appModule(AppModule(this)).build() }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        initFirebase()
        initAdMob()
    }

    private fun initFirebase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    private fun initAdMob() {
        MobileAds.initialize(this, "ca-app-pub-3793327349392749~1846337901")
    }
}