package nl.entreco.dartsscorecard.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import nl.entreco.dartsscorecard.launch.LaunchActivity
import java.util.concurrent.Executors


/**
 * Created by Entreco on 12/12/2017.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Executors.newSingleThreadExecutor().submit {
            SystemClock.sleep(2_000L)
            Handler(Looper.getMainLooper()).post {
                LaunchActivity.launch(this)
            }
        }
    }
}