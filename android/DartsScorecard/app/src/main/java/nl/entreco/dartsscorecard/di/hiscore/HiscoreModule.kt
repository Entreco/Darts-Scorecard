package nl.entreco.dartsscorecard.di.hiscore

import dagger.Module
import dagger.Provides
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.hiscores.HiScoreMapper
import nl.entreco.data.db.hiscores.LocalHiScoresRepository
import nl.entreco.domain.repository.HiScoreRepository

@Module
class HiscoreModule {

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