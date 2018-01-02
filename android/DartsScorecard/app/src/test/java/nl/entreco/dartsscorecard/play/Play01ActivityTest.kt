package nl.entreco.dartsscorecard.play

import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.R
import org.junit.Test

import android.app.onCreate
import org.junit.Ignore

/**
 * Created by Entreco on 02/01/2018.
 */
class Play01ActivityTest {

    @Ignore("almost getting there https://medium.com/@dpreussler/unit-testing-activity-lifecycle-4e740f71e68a")
    @Test
    fun `should inflate layout`() {
        val tested = spy(Play01Activity())
        tested.onCreate(null)
        verify(tested).setContentView(R.layout.activity_play_01)
    }

}