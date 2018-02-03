package nl.entreco.dartsscorecard.di.viewmodel.api

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import nl.entreco.data.api.beta.RemoteFeatureRepository
import nl.entreco.domain.repository.FeatureRepository

/**
 * Created by entreco on 03/02/2018.
 */
@Module
class FeatureApiModule {

    @Provides
    fun provideFbDb() : FeatureRepository {
        return RemoteFeatureRepository(FirebaseDatabase.getInstance())
    }
}