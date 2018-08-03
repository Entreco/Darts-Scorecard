package nl.entreco.dartsscorecard.tv.di.tv

import dagger.Component

@TvScope
@Component(modules = [(TvModule::class)])
interface TvComponent