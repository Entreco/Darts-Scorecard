package nl.entreco.dartsscorecard.di.launch

import dagger.Subcomponent
import nl.entreco.dartsscorecard.launch.LaunchViewModel
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.libads.Ads
import nl.entreco.libads.ui.AdViewModel
import nl.entreco.libconsent.ask.AskConsentUsecase

/**
 * Created by Entreco on 12/12/2017.
 */
@LaunchScope
@Subcomponent(modules = [(LaunchModule::class)])
interface LaunchComponent {
    fun viewModel(): LaunchViewModel
    fun billing(): BillingRepo
    fun ads(): Ads
    fun ask(): AskConsentUsecase
    fun adViewModel(): AdViewModel
}