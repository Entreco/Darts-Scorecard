package nl.entreco.dartsscorecard.di.viewmodel.api

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import nl.entreco.data.wtf.RemoteWtfRepository
import nl.entreco.domain.common.log.Logger
import nl.entreco.domain.repository.WtfRepository

@Module
class FaqApiModule {

    @Provides
    fun provideRemoteFaqRepository(db: FirebaseFirestore, logger: Logger): WtfRepository {
        return RemoteWtfRepository(db, logger)
    }
}