package nl.entreco.dartsscorecard.di.hiscore

import dagger.Subcomponent
import nl.entreco.dartsscorecard.hiscores.HiScoreAdapter
import nl.entreco.dartsscorecard.hiscores.HiScoreNavigator
import nl.entreco.dartsscorecard.hiscores.HiScorePager
import nl.entreco.dartsscorecard.hiscores.HiScoreViewModel

@HiScoreScope
@Subcomponent(modules = [HiscoreModule::class])
interface HiscoreComponent {
    fun viewModel(): HiScoreViewModel
    fun navigator(): HiScoreNavigator
    fun adapter(): HiScoreAdapter
    fun pager(): HiScorePager
}