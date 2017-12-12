package nl.entreco.dartsscorecard.splash

import android.content.Intent
import android.os.Bundle
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.play.Play01Activity


/**
 * Created by Entreco on 12/12/2017.
 */
class SplashActivity : ViewModelActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, Play01Activity::class.java)
        startActivity(intent)
        finish()
    }
}