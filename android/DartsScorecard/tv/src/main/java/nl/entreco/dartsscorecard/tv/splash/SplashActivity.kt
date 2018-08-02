package nl.entreco.dartsscorecard.tv.splash

import android.os.Bundle
import android.support.v4.app.FragmentActivity

class SplashActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LaunchActivity.launch(this)
    }
}