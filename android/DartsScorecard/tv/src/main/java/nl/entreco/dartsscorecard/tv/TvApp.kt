package nl.entreco.dartsscorecard.tv

import android.app.Application
import nl.entreco.dartsscorecard.tv.di.tv.DaggerTvComponent
import nl.entreco.dartsscorecard.tv.di.tv.TvComponent
import nl.entreco.dartsscorecard.tv.di.tv.TvModule

class TvApp : Application() {

    val tvComponent: TvComponent by lazy {
        DaggerTvComponent.builder().tvModule(TvModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        tvComponent.inject(this)
    }

}