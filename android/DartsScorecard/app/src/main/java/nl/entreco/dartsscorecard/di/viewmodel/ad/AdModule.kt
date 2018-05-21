package nl.entreco.dartsscorecard.di.viewmodel.ad

import android.content.Context
import android.content.res.Resources
import com.google.android.gms.ads.InterstitialAd
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.di.beta.BetaScope
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.billing.BillingServiceConnection
import nl.entreco.data.billing.PlayStoreBillingRepository
import nl.entreco.domain.repository.BillingRepository
import javax.inject.Named

@Module
class AdModule {

    @Provides
    fun provideInterstialAd(@ActivityScope context: Context): InterstitialAd {
        return InterstitialAd(context)
    }

    @Provides
    @Named("interstitialId")
    fun provideInterstitialUnitId(@ActivityScope resources: Resources): String {
        return resources.getString(R.string.setup_interstitial_unit_id)
    }
}