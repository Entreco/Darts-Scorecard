package nl.entreco.dartsscorecard.tv.di.launch

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.tv.di.tv.TvScope
import nl.entreco.data.stream.FirebaseSignallingRepository
import nl.entreco.domain.repository.SignallingRepository
import nl.entreco.shared.log.Logger

@Module
class LaunchTvModule {

    @Provides
    @LaunchTvScope
    fun provideSignallingRepository(@TvScope db: FirebaseFirestore, @TvScope logger: Logger) : SignallingRepository {
        return FirebaseSignallingRepository(db, logger)
    }
}