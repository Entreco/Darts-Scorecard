package nl.entreco.dartsscorecard.splash

import org.mockito.kotlin.spy
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Created by entreco on 04/01/2018.
 */
class SplashActivityTest {

    val subject = spy(SplashActivity())

    @Test
    fun `can be created`() {
        assertNotNull(subject)
    }

}