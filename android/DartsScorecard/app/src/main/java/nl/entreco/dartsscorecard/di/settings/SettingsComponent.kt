package nl.entreco.dartsscorecard.di.settings

import dagger.Subcomponent
import nl.entreco.dartsscorecard.settings.SettingsViewModel

@SettingsScope
@Subcomponent(modules = [(SettingsModule::class)])
interface SettingsComponent {
    fun viewModel(): SettingsViewModel
}