package nl.entreco.dartsscorecard.tv.di.tv

import dagger.Component
import nl.entreco.dartsscorecard.tv.TvApp
import nl.entreco.dartsscorecard.tv.di.viewmodel.TvViewModelComponent
import nl.entreco.dartsscorecard.tv.di.viewmodel.TvViewModelModule

@TvScope
@Component(modules = [(TvModule::class)])
interface TvComponent {
    fun inject(app: TvApp)
    fun plus(sub: TvViewModelModule): TvViewModelComponent
}