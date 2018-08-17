package nl.entreco.dartsscorecard.di.tv

import android.app.FragmentManager
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.tv.launch.LaunchTvNavigator
import org.webrtc.SurfaceViewRenderer
import javax.inject.Named

@Module
class TvLaunchModule(private val remoteVideoView: SurfaceViewRenderer) {
    @Provides
    @TvScope
    @Named("remote")
    fun provideRemoteVideoView(): SurfaceViewRenderer {
        return remoteVideoView
    }
}