package nl.entreco.dartsscorecard.di.hiscore

import dagger.Subcomponent
import nl.entreco.dartsscorecard.hiscores.HiScorePager
import nl.entreco.dartsscorecard.hiscores.HiScoreViewModel

@HiScoreScope
@Subcomponent(modules = [HiscoreModule::class])
interface HiscoreComponent {
    fun viewModel() : HiScoreViewModel
//    fun pager() : HiScorePager
}