package nl.entreco.dartsscorecard.play

import dagger.Subcomponent
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.dartsscorecard.play.input.InputViewModel
import nl.entreco.dartsscorecard.play.score.ScoreViewModel

/**
 * Created by Entreco on 14/11/2017.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(Play01Module::class))
interface Play01Component {
    fun viewModel(): Play01ViewModel
}