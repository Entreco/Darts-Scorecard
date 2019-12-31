package nl.entreco.dartsscorecard.di.profile

import dagger.Subcomponent
import nl.entreco.libads.ui.AdViewModel
import nl.entreco.dartsscorecard.profile.view.ProfileViewModel
import nl.entreco.domain.repository.BillingRepo

/**
 * Created by entreco on 21/02/2018.
 */
@ProfileScope
@Subcomponent(modules = [(ProfileModule::class)])
interface ProfileComponent {
    fun viewModel(): ProfileViewModel
    fun billing(): BillingRepo
    fun ads(): AdViewModel
}
