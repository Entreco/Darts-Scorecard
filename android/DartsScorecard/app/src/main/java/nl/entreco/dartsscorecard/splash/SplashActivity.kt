package nl.entreco.dartsscorecard.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.entreco.dartsscorecard.launch.LaunchActivity


/**
 * Created by Entreco on 12/12/2017.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LaunchActivity.launch(this)
    }
}