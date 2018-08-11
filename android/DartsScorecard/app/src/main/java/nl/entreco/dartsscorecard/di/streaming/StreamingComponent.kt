package nl.entreco.dartsscorecard.di.streaming

import dagger.Subcomponent
import nl.entreco.dartsscorecard.streaming.StreamingController

@StreamingScope
@Subcomponent(modules = [(StreamingModule::class)])
interface StreamingComponent {
    fun streamingController(): StreamingController
}