package nl.entreco.dartsscorecard.di.service

import android.app.Service
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.data.stream.FirebaseAnswersRepository
import nl.entreco.data.stream.FirebaseIceRepository
import nl.entreco.data.stream.FirebaseOffersRepository
import nl.entreco.domain.repository.AnswersRepository
import nl.entreco.domain.repository.IceRepository
import nl.entreco.domain.repository.OffersRepository
import nl.entreco.shared.log.Logger
import javax.inject.Named

@Module
class ServiceModule(private val service: Service) {

    @Provides
    @ServiceScope
    fun context(): Context = service

    @Provides
    @ServiceScope
    fun provideIceRepository(@ApplicationScope db: FirebaseDatabase,
                             @ApplicationScope logger: Logger,
                             @ApplicationScope @Named("uuid") uuid: String): IceRepository {
        return FirebaseIceRepository(db, logger, uuid)
    }

    @Provides
    @ServiceScope
    fun provideOffersRepository(@ApplicationScope db: FirebaseDatabase,
                             @ApplicationScope logger: Logger,
                             @ApplicationScope @Named("uuid") uuid: String): OffersRepository {
        return FirebaseOffersRepository(db, logger, uuid)
    }

    @Provides
    @ServiceScope
    fun provideAnswersRepository(@ApplicationScope db: FirebaseDatabase,
                             @ApplicationScope logger: Logger,
                             @ApplicationScope @Named("uuid") uuid: String): AnswersRepository {
        return FirebaseAnswersRepository(db, logger, uuid)
    }
}