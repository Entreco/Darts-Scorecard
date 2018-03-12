package nl.entreco.dartsscorecard.beta.donate

import android.arch.lifecycle.Lifecycle
import android.content.Intent
import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.connect.ConnectToBillingUsecase
import nl.entreco.domain.beta.donations.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 09/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class DonateViewModelTest {

    @Mock private lateinit var mockLifecycle: Lifecycle
    @Mock private lateinit var mockDonateCallback: DonateCallback
    @Mock private lateinit var mockConnectToBillingUsecase: ConnectToBillingUsecase
    @Mock private lateinit var mockFetchDonationsUsecase: FetchDonationsUsecase
    @Mock private lateinit var mockMakeDonationsUsecase: MakeDonationUsecase
    @Mock private lateinit var mockConsumeDonationUsecase: ConsumeDonationUsecase
    @Mock private lateinit var mockAnalytics: Analytics
    private lateinit var subject: DonateViewModel

    private val bindDoneCaptor = argumentCaptor<(Boolean) -> Unit>()
    private val fetchDoneCaptor = argumentCaptor<(FetchDonationsResponse) -> Unit>()
    private val doneMakeCaptor = argumentCaptor<(MakeDonationResponse) -> Unit>()
    private val doneConsumeCaptor = argumentCaptor<(ConsumeDonationResponse) -> Unit>()
    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()

    @Test
    fun `it should register observer`() {
        givenSubject()
        itShouldAddObserver()
    }

    @Test
    fun `it should unregister observer`() {
        givenSubject()
        whenDestroyIsCalled()
        itShouldRemoveObserver()
    }

    @Test
    fun `it should fetch donations when binding to billing succeeds`() {
        givenSubject()
        whenBindingSucceeds()
        thenDonationsAreFetched()
    }

    @Test
    fun `it should NOT fetch donations when binding to billing fails`() {
        givenSubject()
        whenBindingFails()
        thenDonationsAreNotFetched()
    }

    @Test
    fun `it should clear donations when fetching donations succeeds`() {
        givenSubject()
        whenFetchingDonationsSucceeds(emptyList())
        thenDonationsAre(emptyList())
    }

    @Test
    fun `it should track failed when fetching donations fails`() {
        givenSubject()
        whenFetchingDonationsFails()
        thenPurchaseFailedIstracked("FetchDonations failed")
    }

    @Test
    fun `it should do it when unbinding from billing`() {
        givenSubject()
        whenUnbinding()
        thenBillingIsUnbinded()
    }

    @Test
    fun `it should set loading(true) when making donations`() {
        givenSubject()
        whenMakingDonation("sku")
        thenLoadingIs(true)
    }

    @Test
    fun `it should store SKU when making donations`() {
        givenSubject()
        whenMakingDonation("sku")
        thenProductIdIs("sku")
    }

    @Test
    fun `it should execute usecase when making donations`() {
        givenSubject()
        whenMakingDonation("sku")
        thenUsecaseIsExecuted()
    }

    @Test
    fun `it should notify callback when making donations succeeds`() {
        givenSubject()
        whenMakingDonationSucceeds("sku")
        thenCallbackIsNotified()
    }

    @Test
    fun `it should set loading(false) when making donations fails`() {
        givenSubject()
        whenMakingDonationFails(RuntimeException("Alas"))
        thenLoadingIs(false)
    }

    @Test
    fun `it should set loading(false) when make donation fails`() {
        givenSubject()
        whenMakingDonationFails()
        thenLoadingIs(false)
    }

    @Test
    fun `it should clear ProductId when make donation fails`() {
        givenSubject()
        whenMakingDonationFails()
        thenProductIdIs("")
    }

    @Test
    fun `it should track Purchase Failed when make donation fails`() {
        givenSubject()
        whenMakingDonationFails()
        thenPurchaseFailedIstracked("ActivityResult failed")
    }

    @Test
    fun `it should clear ProductId when making donations fails`() {
        givenSubject()
        whenMakingDonationFails(RuntimeException("Alas"))
        thenProductIdIs("")
    }

    @Test
    fun `it should consume donation when donation made`() {
        givenSubject()
        whenDonationMade()
        thenConsumeUsecaseIsExecuted()
    }

    @Test
    fun `it should set loading(false) when consuming donation succeeds`() {
        givenSubject()
        whenConsumingDonationSucceeds("id1", 0)
        thenLoadingIs(false)
    }

    @Test
    fun `it should clear ProductId when consuming donation succeeds`() {
        givenSubject()
        whenConsumingDonationSucceeds("id2", 0)
        thenProductIdIs("")
    }

    @Test
    fun `it should notify callback when consuming donation succeeds`() {
        givenSubject()
        givenDonation("id3")
        whenConsumingDonationSucceeds("id3", 0)
        thenCallbackIsNotifiedOfSuccess()
    }

    @Test
    fun `it should trackPurchase when consuming donation succeeds`() {
        givenSubject()
        givenDonation("id4")
        whenConsumingDonationSucceeds("id4", 0)
        thenPurchaseIsTracked()
    }

    @Test
    fun `it should set loading(false) when consuming donation fails`() {
        givenSubject()
        whenConsumingDonationFails()
        thenLoadingIs(false)
    }

    @Test
    fun `it should clear ProductId when consuming donation fails`() {
        givenSubject()
        whenConsumingDonationFails()
        thenProductIdIs("")
    }

    @Test
    fun `it should track Purchase Failed when consuming donation fails`() {
        givenSubject()
        whenConsumingDonationFails(RuntimeException("Google play services not installed"))
        thenPurchaseFailedIstracked("ConsumeDonation failed")
    }

    @Test
    fun `it should set loading(false) when consuming donation fails (err)`() {
        givenSubject()
        whenConsumingDonationFails(RuntimeException("Google play services not installed"))
        thenLoadingIs(false)
    }

    @Test
    fun `it should clear Productid when consuming donation fails (err)`() {
        givenSubject()
        whenConsumingDonationFails(RuntimeException("Google play services not installed"))
        thenProductIdIs("")
    }

    private fun givenSubject() {
        whenever(mockDonateCallback.lifeCycle()).thenReturn(mockLifecycle)
        subject = DonateViewModel(mockDonateCallback, mockConnectToBillingUsecase, mockFetchDonationsUsecase, mockMakeDonationsUsecase, mockConsumeDonationUsecase, mockAnalytics)
    }

    private fun givenDonation(productId: String) {
        subject.donations.add(Donation("title", "desc", productId, "price", 3, "EUR", "7900000"))
    }

    private fun whenMakingDonation(sku: String) {
        val donation = Donation("donation", "desc", sku, "price", 4, "asf", "122222")
        subject.onDonate(donation)
    }

    private fun whenMakingDonationSucceeds(sku: String) {
        whenMakingDonation(sku)
        verify(mockMakeDonationsUsecase).exec(any(), doneMakeCaptor.capture(), any())
        val response = mock<MakeDonationResponse>()
        doneMakeCaptor.lastValue.invoke(response)
    }

    private fun whenMakingDonationFails(err: Throwable) {
        whenMakingDonation("some sku")
        verify(mockMakeDonationsUsecase).exec(any(), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(err)
    }

    private fun whenDonationMade() {
        val intent = mock<Intent> {
            on { getStringExtra("INAPP_PURCHASE_DATA") } doReturn "Purchase data"
            on { getStringExtra("INAPP_DATA_SIGNATURE") } doReturn "Signature"
        }
        subject.onMakeDonationSuccess(intent)
    }

    private fun whenMakingDonationFails() {
        subject.onMakeDonationFailed()
    }

    private fun whenConsumingDonationSucceeds(productId: String, responseCode: Int) {
        whenDonationMade()
        verify(mockConsumeDonationUsecase).exec(any(), doneConsumeCaptor.capture(), any())
        doneConsumeCaptor.lastValue.invoke(ConsumeDonationResponse(responseCode, productId, "orderId"))
    }

    private fun whenConsumingDonationFails() {
        whenDonationMade()
        verify(mockConsumeDonationUsecase).exec(any(), doneConsumeCaptor.capture(), any())
        doneConsumeCaptor.lastValue.invoke(ConsumeDonationResponse(-1, "productid", "orderId"))
    }

    private fun whenConsumingDonationFails(err: Throwable) {
        whenDonationMade()
        verify(mockConsumeDonationUsecase).exec(any(), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(err)
    }

    private fun whenBindingSucceeds() {
        subject.bind()
        verify(mockConnectToBillingUsecase).bind(bindDoneCaptor.capture())
        bindDoneCaptor.lastValue.invoke(true)
    }

    private fun whenBindingFails() {
        subject.bind()
        verify(mockConnectToBillingUsecase).bind(bindDoneCaptor.capture())
        bindDoneCaptor.lastValue.invoke(false)
    }

    private fun whenFetchingDonationsSucceeds(list: List<Donation>) {
        whenBindingSucceeds()
        verify(mockFetchDonationsUsecase).exec(fetchDoneCaptor.capture(), any())
        fetchDoneCaptor.lastValue.invoke(FetchDonationsResponse(list))
    }

    private fun whenFetchingDonationsFails() {
        whenBindingSucceeds()
        verify(mockFetchDonationsUsecase).exec(any(), failCaptor.capture())
        failCaptor.lastValue.invoke(RuntimeException("Unable to fetch donations"))
    }

    private fun whenDestroyIsCalled() {
        subject.destroy()
    }

    private fun whenUnbinding() {
        subject.unbind()
    }

    private fun itShouldAddObserver() {
        verify(mockDonateCallback).lifeCycle()
        verify(mockLifecycle).addObserver(subject)
    }

    private fun itShouldRemoveObserver() {
        verify(mockLifecycle).removeObserver(subject)
    }

    private fun itShouldTrackViewingDonations() {
        verify(mockAnalytics).trackViewFeature(any())
    }

    private fun thenLoadingIs(expected: Boolean) {
        assertEquals(expected, subject.loading.get())
    }

    private fun thenProductIdIs(expected: String) {
        assertEquals(expected, subject.productId)
    }

    private fun thenDonationsAreFetched() {
        verify(mockFetchDonationsUsecase).exec(any(), any())
    }

    private fun thenDonationsAreNotFetched() {
        verifyZeroInteractions(mockFetchDonationsUsecase)
    }

    private fun thenUsecaseIsExecuted() {
        verify(mockMakeDonationsUsecase).exec(any(), any(), any())
    }

    private fun thenCallbackIsNotified() {
        verify(mockDonateCallback).makeDonation(any())
    }

    private fun thenConsumeUsecaseIsExecuted() {
        verify(mockConsumeDonationUsecase).exec(any(), any(), any())
    }

    private fun thenCallbackIsNotifiedOfSuccess() {
        verify(mockDonateCallback).onDonationMade(any())
    }

    private fun thenPurchaseIsTracked() {
        verify(mockAnalytics).trackPurchase(any(), any())
    }

    private fun thenPurchaseFailedIstracked(step: String) {
        verify(mockAnalytics).trackPurchaseFailed(any(), eq(step))
    }

    private fun thenBillingIsUnbinded() {
        verify(mockConnectToBillingUsecase).unbind()
    }

    private fun thenDonationsAre(expected: List<Donation>) {
        assertEquals(expected.size, subject.donations.size)
    }
}
