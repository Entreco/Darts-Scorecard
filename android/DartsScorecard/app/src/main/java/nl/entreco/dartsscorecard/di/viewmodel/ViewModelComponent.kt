package nl.entreco.dartsscorecard.di.viewmodel

import android.app.Activity
import dagger.Subcomponent
import nl.entreco.dartsscorecard.play.Play01Component
import nl.entreco.dartsscorecard.play.Play01Module
import nl.entreco.dartsscorecard.splash.SplashComponent
import nl.entreco.dartsscorecard.splash.SplashModule

/**
 * Created by Entreco on 14/11/2017.
 */
@Subcomponent(modules = [(ViewModelModule::class)])
interface ViewModelComponent {
    fun inject(activity: Activity)

    // Where can this be used
    fun plus(module: SplashModule): SplashComponent

    fun plus(module: Play01Module): Play01Component
}