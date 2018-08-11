package nl.entreco.dartsscorecard.di.streaming

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.play.stream.StreamFragment
import org.webrtc.SurfaceViewRenderer

@Module
class StreamModule(private val fragment: StreamFragment,
                   private val localVideoView: SurfaceViewRenderer){

    @Provides
    @StreamScope
    fun provideStreamingFragment() : StreamFragment {
        return fragment
    }

    @Provides
    @StreamScope
    fun provideLocalVideoView() : SurfaceViewRenderer {
        return localVideoView
    }
}