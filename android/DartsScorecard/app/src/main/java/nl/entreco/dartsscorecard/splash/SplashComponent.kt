package nl.entreco.dartsscorecard.splash

import dagger.Subcomponent
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope

/**
 * Created by Entreco on 12/12/2017.
 */
@ActivityScope
@Subcomponent(modules = [(SplashModule::class)])
interface SplashComponent {
    fun inject(activity: SplashActivity)
}