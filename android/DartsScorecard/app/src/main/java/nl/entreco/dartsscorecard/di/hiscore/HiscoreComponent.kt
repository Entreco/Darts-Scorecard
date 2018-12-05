package nl.entreco.dartsscorecard.di.hiscore

import dagger.Subcomponent
import nl.entreco.dartsscorecard.hiscores.HiScoreViewModel

@Subcomponent(modules = [HiscoreModule::class])
interface HiscoreComponent {
    fun viewModel() : HiScoreViewModel
}