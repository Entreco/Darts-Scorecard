package nl.entreco.dartsscorecard.di.setup

import dagger.Subcomponent
import nl.entreco.dartsscorecard.ad.AdProvider
import nl.entreco.dartsscorecard.setup.Setup01ViewModel
import nl.entreco.dartsscorecard.setup.players.PlayersViewModel
import nl.entreco.dartsscorecard.setup.settings.SettingsViewModel

/**
 * Created by Entreco on 20/12/2017.
 */
@Setup01Scope
@Subcomponent(modules = [(Setup01Module::class)])
interface Setup01Component {
    fun viewModel(): Setup01ViewModel
    fun players(): PlayersViewModel
    fun settings(): SettingsViewModel
    fun adProvider(): AdProvider
}