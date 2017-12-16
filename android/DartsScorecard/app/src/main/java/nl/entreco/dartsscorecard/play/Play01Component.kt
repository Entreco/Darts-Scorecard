package nl.entreco.dartsscorecard.play

import dagger.Subcomponent
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.dartsscorecard.play.input.InputViewModel
import nl.entreco.dartsscorecard.play.score.ScoreViewModel
import nl.entreco.domain.play.usecase.GetFinishUsecase

/**
 * Created by Entreco on 14/11/2017.
 */
@ActivityScope
@Subcomponent(modules = [(Play01Module::class)])
interface Play01Component {
    fun viewModel(): Play01ViewModel
    fun scoreViewModel(): ScoreViewModel
    fun inputViewModel(): InputViewModel
    fun finishUsecase(): GetFinishUsecase
}