package nl.entreco.dartsscorecard

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import nl.entreco.dartsscorecard.databinding.ActivitySplashBinding

class Play01Activity : AppCompatActivity() {

    lateinit var analytics : FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_play_01).viewModel = Play01ViewModel()
        analytics = FirebaseAnalytics.getInstance(this)
        logDebugEvent()
    }

    private fun logDebugEvent() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "42")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "some name")
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
}
