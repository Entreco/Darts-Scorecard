package nl.entreco.dartsscorecard.di.profile

import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Created by entreco on 21/02/2018.
 */
class ProfileModuleTest {
    @Test
    fun `should not be null`() {
        assertNotNull(ProfileModule())
    }

    @Test
    fun `it should provide mapper`() {
        assertNotNull(ProfileModule().provideMapper())
    }
}
