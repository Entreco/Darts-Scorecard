package nl.entreco.libads

import android.content.Context
import com.google.android.gms.ads.InterstitialAd
import dagger.Module
import dagger.Provides
import nl.entreco.libads.admob.AdMobAds
import nl.entreco.shared.scopes.AppContext
import javax.inject.Named

@Module
object AdModule {

    @Provides
    @JvmStatic
    fun provideAd(@AppContext context: Context): Ads = AdMobAds(context)

    @Provides
    @JvmStatic
    fun provideInterstialAd(@AppContext context: Context): InterstitialAd {
        return InterstitialAd(context)
    }

    @Provides
    @JvmStatic
    @Named("interstitialId")
    fun provideInterstitialUnitId(@AppContext context: Context): String {
        return context.resources.getString(R.string.setup_interstitial_unit_id)
    }
}