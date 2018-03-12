package nl.entreco.dartsscorecard.di.application

import dagger.Component
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelComponent
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelModule

/**
 * Created by Entreco on 14/11/2017.
 */
@ApplicationScope
@Component(modules = [(AppModule::class)])
interface AppComponent {
    fun inject(app: App)
    fun plus(sub: ViewModelModule): ViewModelComponent
}