package nl.entreco.dartsscorecard.di.splash

import dagger.Subcomponent
import nl.entreco.dartsscorecard.splash.SplashViewModel

/**
 * Created by Entreco on 12/12/2017.
 */
@SplashScope
@Subcomponent(modules = [(SplashModule::class)])
interface SplashComponent {
    fun viewModel(): SplashViewModel
}