package nl.entreco.dartsscorecard.tv.splash

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import nl.entreco.dartsscorecard.tv.launch.LaunchTvActivity

class SplashTvActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LaunchTvActivity.launch(this)
    }
}