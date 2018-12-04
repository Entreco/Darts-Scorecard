package nl.entreco.dartsscorecard.di.hiscore

import dagger.Subcomponent
import nl.entreco.dartsscorecard.hiscores.HiscoreViewModel

@Subcomponent(modules = [HiscoreModule::class])
interface HiscoreComponent {
    fun viewModel() : HiscoreViewModel
}