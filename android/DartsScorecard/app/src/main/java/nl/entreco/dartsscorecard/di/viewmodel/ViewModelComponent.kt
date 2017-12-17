package nl.entreco.dartsscorecard.di.viewmodel

import android.app.Activity
import dagger.Subcomponent
import nl.entreco.dartsscorecard.di.play.Play01Component
import nl.entreco.dartsscorecard.di.play.Play01Module
import nl.entreco.dartsscorecard.di.splash.SplashComponent
import nl.entreco.dartsscorecard.di.splash.SplashModule
import nl.entreco.dartsscorecard.di.viewmodel.db.GameDbModule
import nl.entreco.dartsscorecard.di.viewmodel.db.PlayerDbModule
import nl.entreco.dartsscorecard.di.viewmodel.threading.ThreadingModule

/**
 * Created by Entreco on 14/11/2017.
 */
@ActivityScope
@Subcomponent(modules = [(ViewModelModule::class), (ThreadingModule::class), (GameDbModule::class), (PlayerDbModule::class)])
interface ViewModelComponent {
    fun inject(activity: Activity)

    // Where can this be used
    fun plus(module: SplashModule): SplashComponent

    fun plus(module: Play01Module): Play01Component
}