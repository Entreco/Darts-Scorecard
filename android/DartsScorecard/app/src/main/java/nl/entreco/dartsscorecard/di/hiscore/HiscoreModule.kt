package nl.entreco.dartsscorecard.di.hiscore

import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.hiscores.HiScoreActivity
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.hiscores.HiScoreMapper
import nl.entreco.data.db.hiscores.LocalHiScoresRepository
import nl.entreco.domain.repository.HiScoreRepository

@Module
class HiscoreModule(
        private val activity: HiScoreActivity
) {

    @Provides
    @HiScoreScope
    fun provideActivity(): FragmentActivity {
        return activity
    }

    @Provides
    @HiScoreScope
    fun provideHiScoreMapper(): HiScoreMapper {
        return HiScoreMapper()
    }

    @Provides
    @HiScoreScope
    fun provideHiScoreRepository(db: DscDatabase, mapper: HiScoreMapper): HiScoreRepository {
        return LocalHiScoresRepository(db, mapper)
    }
}