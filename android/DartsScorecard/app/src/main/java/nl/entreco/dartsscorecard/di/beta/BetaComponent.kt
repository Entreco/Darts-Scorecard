package nl.entreco.dartsscorecard.di.beta

import dagger.Subcomponent
import nl.entreco.dartsscorecard.beta.BetaAdapter
import nl.entreco.dartsscorecard.beta.BetaViewModel

/**
 * Created by entreco on 30/01/2018.
 */
@BetaScope
@Subcomponent(modules = [(BetaModule::class)])
interface BetaComponent {
    fun viewModel(): BetaViewModel
    fun adapter(): BetaAdapter
}