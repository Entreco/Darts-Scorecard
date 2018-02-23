package nl.entreco.dartsscorecard.di.profile

import dagger.Module
import dagger.Provides
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.profile.LocalProfileRepository
import nl.entreco.data.db.profile.ProfileMapper
import nl.entreco.domain.repository.ProfileRepository

/**
 * Created by entreco on 21/02/2018.
 */
@Module
class ProfileModule {

    @Provides
    @ProfileScope
    fun provideMapper(): ProfileMapper {
        return ProfileMapper()
    }

    @Provides
    @ProfileScope
    fun provideProfileRepository(db: DscDatabase, mapper: ProfileMapper): ProfileRepository {
        return LocalProfileRepository(db, mapper)
    }

}
