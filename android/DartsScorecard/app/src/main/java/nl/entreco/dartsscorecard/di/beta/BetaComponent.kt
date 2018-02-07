package nl.entreco.dartsscorecard.di.beta

import dagger.Subcomponent
import nl.entreco.dartsscorecard.beta.BetaAdapter
import nl.entreco.dartsscorecard.beta.BetaViewModel
import nl.entreco.dartsscorecard.beta.votes.VoteViewModel

/**
 * Created by entreco on 30/01/2018.
 */
@BetaScope
@Subcomponent(modules = [(BetaModule::class)])
interface BetaComponent {
    fun viewModel(): BetaViewModel
    fun votes(): VoteViewModel
    fun adapter(): BetaAdapter
}