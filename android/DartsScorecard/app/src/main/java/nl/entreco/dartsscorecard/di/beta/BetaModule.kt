package nl.entreco.dartsscorecard.di.beta

import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.beta.donate.DonateCallback

/**
 * Created by entreco on 30/01/2018.
 */
@Module
class BetaModule(private val donateCallback: DonateCallback) {

    @Provides
    @BetaScope
    fun provideDonateCallback(): DonateCallback? {
        return donateCallback
    }
}