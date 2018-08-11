package nl.entreco.dartsscorecard.di.service

import android.app.Service
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.data.stream.FirebaseIceRepository
import nl.entreco.domain.repository.IceRepository
import nl.entreco.shared.log.Logger

@Module
class ServiceModule(private val service: Service) {

    @Provides
    @ServiceScope
    fun context(): Context = service

    @Provides
    @ServiceScope
    fun provideIceRepository(@ApplicationScope db: FirebaseDatabase,
                             @ApplicationScope logger: Logger): IceRepository {
        return FirebaseIceRepository(db, logger)
    }
}