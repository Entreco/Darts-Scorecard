package nl.entreco.dartsscorecard.play

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.LocalGameRepository
import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.model.TurnHandler
import nl.entreco.domain.play.repository.GameRepository
import nl.entreco.domain.settings.ScoreSettings

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class Play01Module(private val settings: ScoreSettings, private val teams: Array<Team>){

    @Provides @ActivityScope
    fun provideArbiter() : Arbiter {
        return Arbiter(Score(settings = settings), TurnHandler(teams))
    }

    @Provides @ActivityScope
    fun provideGameRepository() : GameRepository {
        return LocalGameRepository()
    }

    @Provides @ActivityScope
    fun provideTeams() : Array<Team> {
        return teams
    }

    @Provides @ActivityScope
    fun provideScoreSettings() : ScoreSettings {
        return settings
    }
}