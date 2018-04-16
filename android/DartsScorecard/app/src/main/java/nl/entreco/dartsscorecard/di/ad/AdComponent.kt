package nl.entreco.dartsscorecard.di.ad

import dagger.Subcomponent
import nl.entreco.dartsscorecard.ad.AdProvider

@AdScope
@Subcomponent(modules = [(AdModule::class)])
interface AdComponent {
    fun ads(): AdProvider
}