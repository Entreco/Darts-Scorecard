package nl.entreco.dartsscorecard.di.profile

import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 20/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class CreateProfileModuleTest{
    @Test
    fun `it can be instantiated`() {
        assertNotNull(CreateProfileModule())
    }
}