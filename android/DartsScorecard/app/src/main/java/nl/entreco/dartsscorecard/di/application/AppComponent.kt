package nl.entreco.dartsscorecard.di.application

import dagger.Component
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.di.service.ServiceComponent
import nl.entreco.dartsscorecard.di.service.ServiceModule
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelComponent
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelModule
import nl.entreco.libads.AdModule
import nl.entreco.libconsent.di.ConsentModule
import nl.entreco.libcore.scopes.ApplicationScope

/**
 * Created by Entreco on 14/11/2017.
 */
@ApplicationScope
@Component(modules = [AppModule::class, AdModule::class, ConsentModule::class])
interface AppComponent {
    fun inject(app: App)

    fun plus(module: ViewModelModule): ViewModelComponent
    fun plus(module: ServiceModule): ServiceComponent
}