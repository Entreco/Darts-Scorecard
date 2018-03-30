package nl.entreco.dartsscorecard.di.setup

import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by Entreco on 02/01/2018.
 */
@Module
class EditPlayerModule(private val suggestedName: String, private val otherPlayers: LongArray) {

    @Provides
    @Named("suggestion")
    @EditPlayerScope
    fun provideSuggestedName(): String {
        return suggestedName
    }

    @Provides
    @Named("otherPlayers")
    @EditPlayerScope
    fun provideOtherPlayers(): LongArray {
        return otherPlayers
    }
}