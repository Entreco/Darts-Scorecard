package nl.entreco.dartsscorecard.di.streaming

import dagger.Subcomponent
import nl.entreco.dartsscorecard.play.stream.StreamViewModel

@Subcomponent(modules = [(StreamModule::class)])
interface StreamComponent {
    fun viewModel(): StreamViewModel
}