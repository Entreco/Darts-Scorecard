package nl.entreco.dartsscorecard.play

import dagger.Subcomponent
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope

/**
 * Created by Entreco on 14/11/2017.
 */
@ActivityScope
@Subcomponent(modules = [(Play01Module::class)])
interface Play01Component {
    fun viewModel(): Play01ViewModel
}