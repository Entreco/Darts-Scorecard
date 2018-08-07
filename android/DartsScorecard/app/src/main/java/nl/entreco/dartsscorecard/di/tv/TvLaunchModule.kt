package nl.entreco.dartsscorecard.di.tv

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.data.stream.FirebaseSignallingRepository
import nl.entreco.domain.repository.SignallingRepository
import nl.entreco.shared.log.Logger

@Module
class TvLaunchModule {

    @Provides
    @TvScope
    fun provideSignallingServer(@ApplicationScope db: FirebaseFirestore, @ApplicationScope logger: Logger): SignallingRepository {
        return FirebaseSignallingRepository(db, logger)
    }
}