package nl.entreco.libads.admob

import android.content.Context
import com.google.android.gms.ads.MobileAds
import nl.entreco.libads.Ads
import nl.entreco.shared.scopes.ActivityScope
import javax.inject.Inject

class AdMobAds @Inject constructor(
        @ActivityScope private val context: Context
) : Ads {
    override fun init(appId: String) {
            Thread {
                MobileAds.initialize(context.applicationContext, appId)
            }.start()
    }
}