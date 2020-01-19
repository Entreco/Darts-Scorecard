package nl.entreco.dartsscorecard.di.settings

import dagger.Subcomponent
import nl.entreco.dartsscorecard.beta.donate.DonateViewModel
import nl.entreco.dartsscorecard.settings.SettingsViewModel
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.libconsent.ask.AskConsentUsecase

@SettingsScope
@Subcomponent(modules = [(SettingsModule::class)])
interface SettingsComponent {
    fun viewModel(): SettingsViewModel
    fun donate(): DonateViewModel
    fun billing(): BillingRepo
    fun ask(): AskConsentUsecase
}