package nl.entreco.dartsscorecard.di.settings

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.beta.donate.DonateCallback

@Module
class SettingsModule(private val donateCallback: DonateCallback) {

    @Provides
    @SettingsScope
    fun provideDonateCallback(): DonateCallback {
        return donateCallback
    }
}