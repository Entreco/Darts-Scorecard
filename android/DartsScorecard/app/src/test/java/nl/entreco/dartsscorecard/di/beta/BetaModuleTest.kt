package nl.entreco.dartsscorecard.di.beta

import android.content.Context
import nl.entreco.dartsscorecard.beta.donate.DonateCallback
import nl.entreco.data.billing.BillingServiceConnection
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 07/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class BetaModuleTest {

    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockBillingConnection: BillingServiceConnection
    @Mock private lateinit var mockDonateCallback: DonateCallback

    @Test
    fun `it should not be null`() {
        Assert.assertNotNull(BetaModule(mockDonateCallback))
    }

    @Test
    fun `it should provide callback`() {
        assertNotNull(BetaModule(mockDonateCallback).provideDonateCallback())
    }

    @Test
    fun `it should provide service Connection`() {
        assertNotNull(BetaModule(mockDonateCallback).provideServiceConnection())
    }

    @Test
    fun `it should provide BillingRepository`() {
        assertNotNull(BetaModule(mockDonateCallback).provideBillingRepository(mockContext, mockBillingConnection))
    }
}
