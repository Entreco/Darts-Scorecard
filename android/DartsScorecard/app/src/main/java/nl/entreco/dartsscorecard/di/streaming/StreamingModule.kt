package nl.entreco.dartsscorecard.di.streaming

import android.content.Context
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.service.ServiceScope
import org.webrtc.*
import java.util.concurrent.atomic.AtomicBoolean

@Module
class StreamingModule {

    private val factoryInitialized = AtomicBoolean(false)

    @Provides
    @StreamingScope
    fun providePeerConnectionFactory(@ServiceScope context: Context): PeerConnectionFactory {
        if (!factoryInitialized.get()) {
            factoryInitialized.set(true)
            PeerConnectionFactory.initialize(
                    PeerConnectionFactory.InitializationOptions.builder(context)
                            .setEnableInternalTracer(false) // Enabling tracker may cause crashes
                            .setEnableVideoHwAcceleration(true)
                            .createInitializationOptions())
        }

        val options = PeerConnectionFactory.Options()
        return PeerConnectionFactory.builder().setOptions(options).createPeerConnectionFactory()
    }

    @Provides
    @StreamingScope
    fun provideVideoCameraCapturer(@ServiceScope context: Context): CameraVideoCapturer? {
        val enumerator = if (isCamera2Supported(context)) Camera2Enumerator(
                context) else Camera1Enumerator()
        return createCameraCapturerWithFrontAsDefault(enumerator)
    }

    private fun isCamera2Supported(context: Context): Boolean {
        return Camera2Enumerator.isSupported(context)
    }

    private fun createCameraCapturerWithFrontAsDefault(
            enumerator: CameraEnumerator): CameraVideoCapturer? {
        val (frontFacingCameras, backFacingAndOtherCameras) = enumerator.deviceNames
                .partition { enumerator.isFrontFacing(it) }

        return (frontFacingCameras.firstOrNull() ?: backFacingAndOtherCameras.firstOrNull())?.let {
            enumerator.createCapturer(it, null)
        }
    }

}