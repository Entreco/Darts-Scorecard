package nl.entreco.dartsscorecard.di.wtf

import dagger.Subcomponent
import nl.entreco.dartsscorecard.wtf.WtfAdapter
import nl.entreco.dartsscorecard.wtf.WtfViewModel

@WtfScope
@Subcomponent(modules = [(WtfModule::class)])
interface WtfComponent {
    fun viewModel(): WtfViewModel
    fun adapter(): WtfAdapter
}