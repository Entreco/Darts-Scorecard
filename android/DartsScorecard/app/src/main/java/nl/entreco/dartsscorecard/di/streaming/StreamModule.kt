package nl.entreco.dartsscorecard.di.streaming

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.play.stream.StreamFragment
import org.webrtc.CameraVideoCapturer
import org.webrtc.SurfaceViewRenderer
import javax.inject.Named

@Module
class StreamModule(private val fragment: StreamFragment,
                   private val listener: StreamFragment.Listener?,
                   private val localVideoView: SurfaceViewRenderer){

    @Provides
    @StreamScope
    fun provideStreamingFragment() : StreamFragment {
        return fragment
    }

    @Provides
    @StreamScope
    fun provideCameraSwitchHandler() : CameraVideoCapturer.CameraSwitchHandler {
        return fragment
    }

    @Provides
    @StreamScope
    fun provideFragmentListener() : StreamFragment.Listener? {
        return listener
    }

    @Provides
    @StreamScope
    @Named("local")
    fun provideLocalVideoView() : SurfaceViewRenderer {
        return localVideoView
    }
}