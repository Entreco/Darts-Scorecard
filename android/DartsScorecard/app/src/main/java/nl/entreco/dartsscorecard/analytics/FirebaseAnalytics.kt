package nl.entreco.dartsscorecard.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import nl.entreco.domain.Analytics
import javax.inject.Inject

/**
 * Created by Entreco on 15/11/2017.
 */
class FirebaseAnalytics @Inject constructor(context: Context) : Analytics {

    private val fb by lazy { FirebaseAnalytics.getInstance(context) }

    override fun trackAchievement(id: String) {
        fb.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, Bundle().apply { putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, id) })
    }
}