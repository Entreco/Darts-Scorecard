package nl.entreco.libads.admob

import android.content.Context
import android.view.View
import com.google.android.gms.ads.MobileAds
import nl.entreco.libads.Ads
import nl.entreco.libads.ui.AdLoader
import nl.entreco.libconsent.fetch.FetchConsentUsecase
import nl.entreco.shared.scopes.ActivityScope

internal class AdMobAds(
        @ActivityScope private val context: Context,
        private val adLoader: AdLoader
) : Ads {


    override fun init(appId: String) {
        Thread {
            MobileAds.initialize(context.applicationContext, appId)
        }.start()
    }

    override fun load(view: View, done: (Boolean) -> Unit) {
        adLoader.loadAd(view, object : AdLoader.AdListener {
            override fun onAdLoaded() {
                done(true)
            }

            override fun onAdFailed() {
                done(false)
            }
        })
    }
}