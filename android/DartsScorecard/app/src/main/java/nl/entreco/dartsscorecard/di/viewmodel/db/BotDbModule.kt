package nl.entreco.dartsscorecard.di.viewmodel.db

import dagger.Module
import dagger.Provides
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.bot.BotMapper
import nl.entreco.data.db.bot.LocalBotRepository
import nl.entreco.domain.repository.BotRepository
import nl.entreco.shared.scopes.ActivityScope

@Module
class BotDbModule {

    @Provides
    @ActivityScope
    fun provideBotMapper() = BotMapper()

    @Provides
    @ActivityScope
    fun provideBotRepository(db: DscDatabase, mapper: BotMapper): BotRepository {
        return LocalBotRepository(db, mapper)
    }
}