package nl.entreco.dartsscorecard.di.hiscore

import androidx.fragment.app.FragmentManager
import dagger.Module
import dagger.Provides
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.hiscores.HiScoreMapper
import nl.entreco.data.db.hiscores.LocalHiScoresRepository
import nl.entreco.domain.repository.HiScoreRepository

@Module
class HiscoreModule(private val fm: FragmentManager) {

//    @Provides
//    @HiScoreScope
//    fun provideFragmentManager() : FragmentManager {
//        return fm
//    }

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