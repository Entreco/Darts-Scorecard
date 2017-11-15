package nl.entreco.dartsscorecard.analytics

import android.content.Context
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

/**
 * Created by Entreco on 15/11/2017.
 */
class FirebaseAnalytics @Inject constructor(context: Context): Analytics {

    private val fb = FirebaseAnalytics.getInstance(context)

    override fun track(event: String) {
        Log.d("FirebaseAnalytics", "track: $event fb: $fb")
    }
}