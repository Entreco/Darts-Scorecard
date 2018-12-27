package nl.entreco.dartsscorecard.ad

import androidx.lifecycle.Lifecycle
import com.google.android.gms.ads.AdView
import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.ad.FetchPurchasedItemsResponse
import nl.entreco.domain.ad.FetchPurchasedItemsUsecase
import nl.entreco.domain.purchases.connect.ConnectToBillingUsecase
import nl.entreco.liblog.Logger
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AdViewModelTest {

    @Mock private lateinit var mockAdView: AdView
    @Mock private lateinit var mockBillingUsecas: ConnectToBillingUsecase
    @Mock private lateinit var mockFetchItemsUsecase: FetchPurchasedItemsUsecase
    @Mock private lateinit var mockAdLoader: AdLoader
    @Mock private lateinit var mockInterstitialLoader: InterstitialLoader
    @Mock private lateinit var mockLogger: Logger
    @Mock private lateinit var mockLifecycle: Lifecycle
    private lateinit var subject : AdViewModel

    @Test
    fun `it should register on init`() {
        givenSubject()
        thenObserverIsAdded()
    }

    @Test
    fun `it should deregister on destroy`() {
        givenSubject()
        whenDestroying()
        thenObserverIsRemoved()
    }

    @Test
    fun `it should not serve adds initially`() {
        givenSubject()
        whenLoadingAdSucceeds()
        thenShowAdIs(false)
    }

    @Test
    fun `it should NOT serve adds when purchasedItems exist`() {
        givenSubject()
        givenPurchasedItems(true)
        whenLoadingAdSucceeds()
        thenShowAdIs(false)
    }

    @Test
    fun `it should serve adds when no purchasedItems exist`() {
        givenSubject()
        givenPurchasedItems(false)
        whenLoadingAdSucceeds()
        thenShowAdIs(true)
    }

    @Test
    fun `it should not serve adds when loading fails`() {
        givenSubject()
        whenLoadingAdFails()
        thenShowAdIs(false)
    }

    @Test
    fun `it should NOT serve interstitial initially`() {
        givenSubject()
        whenProvidingInterstitials()
        thenNoInterstialIsLoaded()
    }

    @Test
    fun `it should NOT serve interstitial when unable to bind`() {
        givenSubject()
        givenBindingToBillingFails()
        whenProvidingInterstitials()
        thenNoInterstialIsLoaded()
    }

    @Test
    fun `it should serve interstitial when no items purchased`() {
        givenSubject()
        givenPurchasedItems(false)
        whenProvidingInterstitials()
        thenInterstialIsLoaded()
    }

    @Test
    fun `it should NOT serve interstitial when items purchased`() {
        givenSubject()
        givenPurchasedItems(true)
        whenProvidingInterstitials()
        thenNoInterstialIsLoaded()
    }

    @Test
    fun `it should NOT serve interstitial when items cannot be retrieved`() {
        givenSubject()
        givenPurchasedItemsFails()
        whenProvidingInterstitials()
        thenNoInterstialIsLoaded()
    }

    private fun givenSubject() {
        subject = AdViewModel(mockLifecycle, mockBillingUsecas, mockFetchItemsUsecase, mockAdLoader, mockInterstitialLoader, mockLogger, false)
    }

    private fun givenPurchasedItems(purchasedItems: Boolean){
        val bindCaptor = argumentCaptor<(Boolean)->Unit>()
        val doneCaptor = argumentCaptor<(FetchPurchasedItemsResponse)->Unit>()
        subject.bind()
        verify(mockBillingUsecas).bind(bindCaptor.capture())
        bindCaptor.lastValue.invoke(true)
        verify(mockFetchItemsUsecase).exec(doneCaptor.capture(), any())
        doneCaptor.lastValue.invoke(FetchPurchasedItemsResponse(!purchasedItems))
    }

    private fun givenPurchasedItemsFails(){
        val bindCaptor = argumentCaptor<(Boolean)->Unit>()
        val failCaptor = argumentCaptor<(Throwable)->Unit>()
        subject.bind()
        verify(mockBillingUsecas).bind(bindCaptor.capture())
        bindCaptor.lastValue.invoke(true)
        verify(mockFetchItemsUsecase).exec(any(), failCaptor.capture())
        failCaptor.lastValue.invoke(RuntimeException("Unable to query purchased items"))
    }

    private fun givenBindingToBillingFails(){
        val bindCaptor = argumentCaptor<(Boolean)->Unit>()
        subject.bind()
        verify(mockBillingUsecas).bind(bindCaptor.capture())
        bindCaptor.lastValue.invoke(false)
    }

    private fun whenLoadingAdSucceeds() {
        val listenerCaptor = argumentCaptor<AdLoader.AdListener>()
        subject.provideAdd(mockAdView)
        verify(mockAdLoader).loadAd(eq(mockAdView), listenerCaptor.capture())
        listenerCaptor.lastValue.onAdLoaded()
    }

    private fun whenLoadingAdFails() {
        val listenerCaptor = argumentCaptor<AdLoader.AdListener>()
        subject.provideAdd(mockAdView)
        verify(mockAdLoader).loadAd(eq(mockAdView), listenerCaptor.capture())
        listenerCaptor.lastValue.onAdFailed()
    }

    private fun whenProvidingInterstitials() {
        subject.provideInterstitial()
    }

    private fun whenDestroying() {
        subject.destroy()
    }

    private fun thenObserverIsAdded() {
        verify(mockLifecycle).addObserver(subject)
    }

    private fun thenObserverIsRemoved() {
        verify(mockLifecycle).removeObserver(subject)
    }

    private fun thenNoInterstialIsLoaded() {
        verifyZeroInteractions(mockInterstitialLoader)
    }

    private fun thenInterstialIsLoaded() {
        verify(mockInterstitialLoader).showInterstitial()
    }

    private fun thenShowAdIs(expected: Boolean){
        assertEquals(expected, subject.showAd.get())
    }
}
