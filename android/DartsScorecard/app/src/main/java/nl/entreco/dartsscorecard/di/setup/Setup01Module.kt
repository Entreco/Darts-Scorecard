package nl.entreco.dartsscorecard.di.setup

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.setup.Setup01Activity
import nl.entreco.dartsscorecard.setup.Setup01Navigator
import nl.entreco.dartsscorecard.setup.players.PlayerEditor

/**
 * Created by Entreco on 20/12/2017.
 */
@Module
class Setup01Module(activity: Setup01Activity) {

    private val navigator = Setup01Navigator(activity)

    @Provides
    @Setup01Scope
    fun provideNavigator(): Setup01Navigator {
        return navigator
    }

    @Provides
    @Setup01Scope
    fun providePlayerEditor(): PlayerEditor {
        return navigator
    }
}