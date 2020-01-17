package nl.entreco.dartsscorecard.di.launch

import android.app.Activity
import com.nhaarman.mockito_kotlin.mock
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Created by Entreco on 02/01/2018.
 */
class LaunchModuleTest {

    private val mockListener: (MakePurchaseResponse) -> Unit = mock()
    private val mockActivity: Activity = mock()

    @Test
    fun `it should not be null`() {
        assertNotNull(LaunchModule(mockActivity, mockListener))
    }
}