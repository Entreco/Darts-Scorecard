package nl.entreco.dartsscorecard.play

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.dartsscorecard.play.score.ReadyListener
import nl.entreco.data.LocalGameRepository
import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.model.TurnHandler
import nl.entreco.domain.play.repository.GameRepository
import nl.entreco.domain.settings.ScoreSettings
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class Play01Module(private val settings: ScoreSettings, private val teams: Array<Team>, private val onReady: ReadyListener){

    @Provides @ActivityScope
    fun provideArbiter() : Arbiter {
        return Arbiter(Score(settings = settings), TurnHandler(teams, settings.teamStartIndex))
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

    @Provides @ActivityScope
    fun provideReadyListener(): ReadyListener {
        return onReady
    }

    @Provides @ActivityScope
    fun provideExecutorService() : ExecutorService {
        return Executors.newSingleThreadExecutor()
    }
}