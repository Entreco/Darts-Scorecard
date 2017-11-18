package nl.entreco.dartsscorecard.play

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.dartsscorecard.play.score.ScoreKeeper
import nl.entreco.dartsscorecard.play.score.ScoreViewModel
import nl.entreco.data.LocalGameRepository
import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.repository.GameRepository
import nl.entreco.domain.settings.ScoreSettings

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class Play01Module(private val numPlayers: Int, private val settings: ScoreSettings){
    @Provides @ActivityScope
    fun provideArbiter() : Arbiter {
        return Arbiter(Score(settings = settings), numPlayers)
    }

    @Provides @ActivityScope
    fun provideGameRepository() : GameRepository {
        return LocalGameRepository()
    }

    @Provides @ActivityScope
    fun provideScoreSettings() : ScoreSettings {
        return settings
    }
}