package nl.entreco.dartsscorecard.di.faq

import dagger.Subcomponent
import nl.entreco.dartsscorecard.faq.WtfAdapter
import nl.entreco.dartsscorecard.faq.WtfViewModel

@WtfScope
@Subcomponent(modules = [(WtfModule::class)])
interface WtfComponent {
    fun viewModel(): WtfViewModel
    fun adapter(): WtfAdapter
}