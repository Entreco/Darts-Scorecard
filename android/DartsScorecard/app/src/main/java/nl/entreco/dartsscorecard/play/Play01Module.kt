package nl.entreco.dartsscorecard.play

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.domain.play.Arbiter

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class Play01Module(private val arbiter: Arbiter){
    @Provides @ActivityScope
    fun provideArbiter() : Arbiter {
        return arbiter
    }
}