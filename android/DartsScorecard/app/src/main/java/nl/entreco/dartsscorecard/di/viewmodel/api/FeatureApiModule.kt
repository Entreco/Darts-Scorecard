package nl.entreco.dartsscorecard.di.viewmodel.api

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import nl.entreco.data.api.beta.RemoteFeatureRepository
import nl.entreco.shared.log.Logger
import nl.entreco.domain.repository.FeatureRepository

/**
 * Created by entreco on 03/02/2018.
 */
@Module
class FeatureApiModule {

    @Provides
    fun provideRemoteFeatureRepository(db: FirebaseFirestore, logger: Logger): FeatureRepository {
        return RemoteFeatureRepository(db, logger)
    }
}