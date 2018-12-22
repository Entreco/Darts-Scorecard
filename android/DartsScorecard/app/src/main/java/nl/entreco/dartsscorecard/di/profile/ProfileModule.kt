package nl.entreco.dartsscorecard.di.profile

import android.content.ContentResolver
import android.content.Context
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.profile.LocalProfileInfoRepository
import nl.entreco.data.db.profile.LocalProfileStatRepository
import nl.entreco.data.db.profile.ProfileMapper
import nl.entreco.data.db.profile.ProfileStatMapper
import nl.entreco.data.image.LocalImageRepository
import nl.entreco.domain.repository.ImageRepository
import nl.entreco.domain.repository.ProfileInfoRepository
import nl.entreco.domain.repository.ProfileStatRepository

/**
 * Created by entreco on 21/02/2018.
 */
@Module
class ProfileModule {

    @Provides
    @ProfileScope
    fun provideContentResolver(@ActivityScope context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @ProfileScope
    fun provideProfileMapper(): ProfileMapper {
        return ProfileMapper()
    }

    @Provides
    @ProfileScope
    fun provideProfileStatMapper(): ProfileStatMapper {
        return ProfileStatMapper()
    }

    @Provides
    @ProfileScope
    fun provideProfileStatRepository(db: DscDatabase, mapper: ProfileStatMapper): ProfileStatRepository {
        return LocalProfileStatRepository(db, mapper)
    }

    @Provides
    @ProfileScope
    fun provideProfileRepository(db: DscDatabase, mapper: ProfileMapper): ProfileInfoRepository {
        return LocalProfileInfoRepository(db, mapper)
    }

    @Provides
    @ProfileScope
    fun provideImageRepository(@ActivityScope context: Context, contentResolver: ContentResolver): ImageRepository {
        return LocalImageRepository(context, contentResolver)
    }

}
