package nl.entreco.data.billing

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsResponseListener
import com.android.vending.billing.IInAppBillingService
import com.google.gson.GsonBuilder
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.MakeDonationResponse
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 18/02/2018.
 */
class PlayStoreBillingRepositoryOldTest {

    private val mockBillingClient: BillingClient = mock()
    private val mockContext: Activity = mock()
    private val mockServiceConnection: GooglePlayConnection = mock{
        on { getClient() } doReturn mockBillingClient
    }
    private val mockInappBillingService: IInAppBillingService = mock()
    private val mockPendingIntent: PendingIntent = mock()
    private val mockBundle: Bundle = mock()
    private val mockDoneCallback: (Boolean) -> Unit = mock()

    private lateinit var subject: PlayStoreBillingRepository
    private lateinit var expectedResponse: MakeDonationResponse
    private var expectedConsumtionResponse: Int = 0
    private var expectedPurchasedItems = emptyList<String>()

    private var mockFetchDone : (List<Donation>)->Unit = mock()

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

    @Test
    fun `it should report result when query purchase items succeeds`() {
        givenSubject()
        whenFetchingPurchasesSucceeds("item§1", "item 2")
        assertNotNull(expectedPurchasedItems)
    }

    @Test(expected = Throwable::class)
    fun `it should throw exception when query purchase items fails`() {
        givenSubject()
        whenFetchingPurchasesFails()
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
        val queryCaptor = argumentCaptor<(BillingResult, List<SkuDetails>)->Unit>()
        subject.fetchDonationsExclAds(mockFetchDone)
        verify(mockBillingClient).querySkuDetailsAsync(any(), queryCaptor.capture())
    }

    private fun whenFetchingDonationsFails() {
        whenever(mockServiceConnection.getService()).thenReturn(mockInappBillingService)
        subject.fetchDonationsExclAds()
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

    private fun whenFetchingPurchasesSucceeds(vararg purchases: String) {
        whenever(mockBundle.getStringArrayList("INAPP_PURCHASE_ITEM_LIST")).thenReturn(ArrayList(purchases.map { it }))
        whenever(mockInappBillingService.getPurchases(any(), eq(null), eq("inapp"), eq("unused token"))).thenReturn(mockBundle)
        whenever(mockServiceConnection.getService()).thenReturn(mockInappBillingService)
        expectedPurchasedItems = subject.fetchPurchasedItems()
    }

    private fun whenFetchingPurchasesFails() {
        whenever(mockServiceConnection.getService()).thenReturn(mockInappBillingService)
        subject.fetchPurchasedItems()
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
