package nl.entreco.data.billing

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import com.android.vending.billing.IInAppBillingService
import com.google.gson.GsonBuilder
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.MakeDonationResponse
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 18/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class PlayStoreBillingRepositoryTest {

    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockServiceConnection: BillingServiceConnection
    @Mock private lateinit var mockInappBillingService: IInAppBillingService
    @Mock private lateinit var mockPendingIntent: PendingIntent
    @Mock private lateinit var mockBundle: Bundle
    @Mock private lateinit var mockDoneCallback: (Boolean) -> Unit

    private lateinit var subject: PlayStoreBillingRepository
    private lateinit var expectedResponse: MakeDonationResponse
    private var expectedConsumtionResponse: Int = 0
    private var expectedDonation = emptyList<Donation>()

    private val gson = GsonBuilder().create()

    @Test
    fun `it should set callback when binding`() {
        givenSubject()
        whenBindingToBillingService()
        thenCallbackIsSetOnService()
    }

    @Test
    fun `it should start service when binding`() {
        givenSubject()
        whenBindingToBillingService()
        thenServiceIsStarted()
    }

    @Test
    fun `it should clear callback when unbinding`() {
        givenSubject()
        whenUnbindingToBillingService()
        thenCallbackIsClearedOnService()
    }

    @Test
    fun `it should stop service when unbinding`() {
        givenSubject()
        whenUnbindingToBillingService()
        thenServiceIsStopped()
    }

    @Test
    fun `it should report result when fetching donations`() {
        givenSubject()
        whenFetchingDonationsSucceeds()
        thenDonationIsReturned()
    }

    @Test(expected = Throwable::class)
    fun `it should throw exception when fetching donations with invalid bundle`() {
        givenSubject()
        whenFetchingDonationsFails()
    }

    @Test
    fun `it should report result when making donations`() {
        givenSubject()
        whenDonationsSucceeds()
        thenMakeResponseIsReturned()
    }

    @Test(expected = Throwable::class)
    fun `it should throw exception when donating with invalid bundle`() {
        givenSubject()
        whenDonatingFails()
    }


    @Test
    fun `it should report result when consuming donations`() {
        givenSubject()
        whenConsumptionSucceeds()
        thenConsumptionResponseIsReturned()
    }

    @Test(expected = NullPointerException::class)
    fun `it should throw exception when consuming with invalid bundle`() {
        givenSubject()
        whenConsumingFails()
    }

    private fun givenSubject() {
        subject = PlayStoreBillingRepository(mockContext, mockServiceConnection)
    }

    private fun whenBindingToBillingService() {
        subject.bind(mockDoneCallback)
    }

    private fun whenUnbindingToBillingService() {
        subject.unbind()
    }

    private fun whenFetchingDonationsSucceeds() {
        val product = gson.toJson(DonationApiData("10_feature_votes", "price", "title", "desc", "$", "120000"))

        whenever(mockBundle.getStringArrayList("DETAILS_LIST")).thenReturn(arrayListOf(product))
        whenever(mockInappBillingService.getSkuDetails(any(), eq(null), any(), any())).thenReturn(mockBundle)
        whenever(mockServiceConnection.getService()).thenReturn(mockInappBillingService)

        expectedDonation = subject.fetchDonations()
    }

    private fun whenFetchingDonationsFails() {
        whenever(mockServiceConnection.getService()).thenReturn(mockInappBillingService)
        subject.fetchDonations()
    }

    private fun whenDonationsSucceeds() {
        whenever(mockBundle.getParcelable<PendingIntent>("BUY_INTENT")).thenReturn(mockPendingIntent)
        whenever(mockInappBillingService.getBuyIntent(any(), eq(null), any(), any(), any())).thenReturn(mockBundle)
        whenever(mockServiceConnection.getService()).thenReturn(mockInappBillingService)
        expectedResponse = subject.donate(Donation("ti", "d", "s", "p", 10, "GBP", "100000"))
    }

    private fun whenDonatingFails() {
        whenever(mockServiceConnection.getService()).thenReturn(mockInappBillingService)
        subject.donate(Donation("ti", "d", "s", "p", 10, "EUR", "12212222"))
    }

    private fun whenConsumptionSucceeds() {
        whenever(mockInappBillingService.consumePurchase(any(), eq(null), eq("token"))).thenReturn(0)
        whenever(mockServiceConnection.getService()).thenReturn(mockInappBillingService)
        expectedConsumtionResponse = subject.consume("token")
    }

    private fun whenConsumingFails() {
        whenever(mockServiceConnection.getService()).thenReturn(null)
        subject.consume("token")
    }

    private fun thenCallbackIsSetOnService() {
        verify(mockServiceConnection).setCallback(mockDoneCallback)
    }

    private fun thenCallbackIsClearedOnService() {
        verify(mockServiceConnection).setCallback(any())
    }

    private fun thenServiceIsStarted() {
        verify(mockContext).bindService(any(), eq(mockServiceConnection), eq(Context.BIND_AUTO_CREATE))
    }

    private fun thenServiceIsStopped() {
        verify(mockContext).unbindService(mockServiceConnection)
    }

    private fun thenDonationIsReturned() {
        assertNotNull(expectedDonation)
        assertFalse(expectedDonation.isEmpty())
    }

    private fun thenMakeResponseIsReturned() {
        assertNotNull(expectedResponse)
    }

    private fun thenConsumptionResponseIsReturned() {
        assertNotNull(expectedConsumtionResponse)
    }

}
