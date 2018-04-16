package nl.entreco.dartsscorecard.di.ad

import android.content.Context
import android.content.res.Resources
import com.google.android.gms.ads.InterstitialAd
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.R
import javax.inject.Named

@Module
class AdModule(private val resources: Resources) {

    @Provides
    @AdScope
    fun provideInterstialAd(context: Context): InterstitialAd {
        return InterstitialAd(context)
    }

    @Provides
    @AdScope
    @Named("interstitialId")
    fun provideInterstitialUnitId(): String {
        return resources.getString(R.string.setup_interstitial_unit_id)
    }

    @Provides
    @AdScope
    @Named("adId")
    fun provideAdUnitId(): String {
        return resources.getString(R.string.setup_ad_unit_id)
    }
}