package nl.entreco.dartsscorecard.di.play

import dagger.Subcomponent
import nl.entreco.dartsscorecard.play.Play01Navigator
import nl.entreco.dartsscorecard.play.Play01ViewModel
import nl.entreco.dartsscorecard.play.input.InputViewModel
import nl.entreco.dartsscorecard.play.score.ScoreViewModel
import nl.entreco.dartsscorecard.play.live.LiveStatViewModel
import nl.entreco.dartsscorecard.play.stream.ToggleStreamViewModel
import nl.entreco.domain.play.finish.GetFinishUsecase

/**
 * Created by Entreco on 14/11/2017.
 */
@Play01Scope
@Subcomponent(modules = [(Play01Module::class)])
interface Play01Component {
    fun viewModel(): Play01ViewModel
    fun navigator(): Play01Navigator
    fun scoreViewModel(): ScoreViewModel
    fun streamViewModel(): ToggleStreamViewModel
    fun inputViewModel(): InputViewModel
    fun statViewModel(): LiveStatViewModel
    fun finishUsecase(): GetFinishUsecase
}