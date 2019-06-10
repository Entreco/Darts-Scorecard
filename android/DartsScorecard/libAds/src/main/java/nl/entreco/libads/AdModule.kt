package nl.entreco.libads

import android.content.Context
import dagger.Module
import dagger.Provides
import nl.entreco.libads.admob.AdMobAds
import nl.entreco.libads.admob.AdMobInterstitials
import nl.entreco.libads.ui.AdLoader
import nl.entreco.libconsent.fetch.FetchConsentUsecase
import nl.entreco.shared.scopes.AppContext
import javax.inject.Named

@Module
object AdModule {
    
    @Provides
    @JvmStatic
    fun provideAds(@AppContext context: Context, fetch: FetchConsentUsecase): Ads {
        val loader = AdLoader(fetch)
        return AdMobAds(context, loader)
    }

    @Provides
    @JvmStatic
    fun provideInterstials(@AppContext context: Context): Interstitials = AdMobInterstitials(context)

    @Provides
    @JvmStatic
    @Named("interstitialId")
    fun provideInterstitialUnitId(@AppContext context: Context): String {
        return context.resources.getString(R.string.setup_interstitial_unit_id)
    }
}