package nl.entreco.dartsscorecard.di.setup

import android.content.Context
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.setup.Setup01Activity
import nl.entreco.dartsscorecard.setup.Setup01Navigator
import nl.entreco.dartsscorecard.setup.players.PlayerEditor
import nl.entreco.data.setup.repository.SharedPreferenceRepo
import nl.entreco.domain.repository.PreferenceRepository

/**
 * Created by Entreco on 20/12/2017.
 */
@Module
class Setup01Module(activity: Setup01Activity) {

    private val navigator = Setup01Navigator(activity)
    private val prefs = activity.getSharedPreferences("setup", Context.MODE_PRIVATE)

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

    @Provides
    @Setup01Scope
    fun providePreferenceRepo(): PreferenceRepository {
        return SharedPreferenceRepo(prefs)
    }
}