package nl.entreco.dartsscorecard.di.beta

import android.arch.lifecycle.Lifecycle
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 07/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class BetaModuleTest{

    @Mock private lateinit var mockLifecycle : Lifecycle

    @Test
    fun `it should not be null`() {
        Assert.assertNotNull(BetaModule(mockLifecycle))
    }
}