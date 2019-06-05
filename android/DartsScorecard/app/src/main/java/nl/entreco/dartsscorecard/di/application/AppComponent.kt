package nl.entreco.dartsscorecard.di.application

import dagger.Component
import nl.entreco.dartsscorecard.App
import nl.entreco.dartsscorecard.di.service.ServiceComponent
import nl.entreco.dartsscorecard.di.service.ServiceModule
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelComponent
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelModule
import nl.entreco.libads.AdModule
import nl.entreco.libads.Ads
import nl.entreco.libconsent.ask.AskConsentUsecase
import nl.entreco.libconsent.di.FetchConsentModule
import nl.entreco.libconsent.fetch.FetchCurrentConsentUsecase
import nl.entreco.shared.scopes.ApplicationScope

/**
 * Created by Entreco on 14/11/2017.
 */
@ApplicationScope
@Component(modules = [AppModule::class, AdModule::class, FetchConsentModule::class])
interface AppComponent {
    fun inject(app: App)
    fun ads(): Ads
    fun fetch(): FetchCurrentConsentUsecase
    fun ask(): AskConsentUsecase

    fun plus(sub: ViewModelModule): ViewModelComponent
    fun plus(sub: ServiceModule): ServiceComponent
}