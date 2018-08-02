package nl.entreco.dartsscorecard.tv.di.viewmodel

import dagger.Subcomponent
import nl.entreco.dartsscorecard.tv.di.launch.LaunchTvComponent
import nl.entreco.dartsscorecard.tv.di.launch.LaunchTvModule
import nl.entreco.dartsscorecard.tv.di.viewmodel.threading.TvThreadingModule

@TvActivityScope
@Subcomponent(modules = [TvViewModelModule::class, TvThreadingModule::class])
interface TvViewModelComponent {
    fun plus(module: LaunchTvModule) : LaunchTvComponent
}