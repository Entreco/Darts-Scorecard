package nl.entreco.dartsscorecard.play

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.LocalGameRepository
import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.repository.GameRepository

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class Play01Module(private val arbiter: Arbiter){
    @Provides @ActivityScope
    fun provideArbiter() : Arbiter {
        return arbiter
    }

    @Provides @ActivityScope
    fun provideGameRepository() : GameRepository {
        return LocalGameRepository()
    }
}