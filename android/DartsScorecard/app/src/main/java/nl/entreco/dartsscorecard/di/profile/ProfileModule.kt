package nl.entreco.dartsscorecard.di.profile

import android.content.ContentResolver
import android.content.Context
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.profile.LocalProfileRepository
import nl.entreco.data.db.profile.ProfileMapper
import nl.entreco.data.image.LocalImageRepository
import nl.entreco.domain.repository.ImageRepository
import nl.entreco.domain.repository.ProfileRepository

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
    fun provideMapper(): ProfileMapper {
        return ProfileMapper()
    }

    @Provides
    @ProfileScope
    fun provideProfileRepository(db: DscDatabase, mapper: ProfileMapper): ProfileRepository {
        return LocalProfileRepository(db, mapper)
    }

    @Provides
    @ProfileScope
    fun provideImageRepository(@ActivityScope context: Context, contentResolver: ContentResolver): ImageRepository {
        return LocalImageRepository(context, contentResolver)
    }

}
