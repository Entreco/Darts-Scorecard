package nl.entreco.dartsscorecard.di.setup

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.setup.Setup01Activity
import nl.entreco.dartsscorecard.setup.Setup01Navigator

/**
 * Created by Entreco on 20/12/2017.
 */
@Module
class Setup01Module(private val activity: Setup01Activity) {
    @Provides
    @Setup01Scope
    fun provideNavigator(): Setup01Navigator {
        return Setup01Navigator(activity)
    }
}