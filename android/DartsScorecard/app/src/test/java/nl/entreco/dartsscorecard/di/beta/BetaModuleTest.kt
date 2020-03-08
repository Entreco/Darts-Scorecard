package nl.entreco.dartsscorecard.di.beta

import android.app.Activity
import com.nhaarman.mockito_kotlin.mock
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.liblog.Logger
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Created by entreco on 07/02/2018.
 */
class BetaModuleTest {

    private val mockLogger: Logger = mock()
    private val mockDonateCallback: (MakePurchaseResponse) -> Unit = mock()
    private val mockActivity: Activity = mock()

    @Test
    fun `it should not be null`() {
        Assert.assertNotNull(BetaModule(mockActivity, mockDonateCallback))
    }

    @Test
    fun `it should provide callback`() {
        assertNotNull(BetaModule(mockActivity, mockDonateCallback).provideBillingService(mockLogger))
    }
}
