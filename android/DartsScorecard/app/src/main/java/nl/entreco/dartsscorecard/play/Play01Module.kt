package nl.entreco.dartsscorecard.play

import dagger.Module
import dagger.Provides
import nl.entreco.domain.play.Arbiter
import javax.inject.Singleton

/**
 * Created by Entreco on 14/11/2017.
 */
@Singleton
@Module
class Play01Module(private val arbiter: Arbiter){
    @Provides
    fun provideArbiter() : Arbiter {
        return arbiter
    }
}