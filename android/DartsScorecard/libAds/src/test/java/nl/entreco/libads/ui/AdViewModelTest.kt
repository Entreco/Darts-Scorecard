package nl.entreco.libads.ui

import androidx.lifecycle.Lifecycle
import com.google.android.gms.ads.AdView
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyZeroInteractions
import nl.entreco.domain.ad.FetchPurchasedItemsResponse
import nl.entreco.domain.ad.FetchPurchasedItemsUsecase
import nl.entreco.domain.purchases.connect.ConnectToBillingUsecase
import nl.entreco.libconsent.fetch.FetchConsentUsecase
import org.junit.Assert.*
import org.junit.Test

class AdViewModelTest {

    private val mockAdView: AdView = mock()
    private val mockBillingUsecas: ConnectToBillingUsecase = mock()
    private val mockFetchItemsUsecase: FetchPurchasedItemsUsecase = mock()
    private val mockFetchConsent: FetchConsentUsecase = mock()
    private val mockAdLoader: AdLoader = mock()
    private val mockInterstitialLoader: InterstitialLoader = mock()
    private val mockLifecycle: Lifecycle = mock()
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
        subject = AdViewModel(mockLifecycle, mockBillingUsecas, mockFetchItemsUsecase, mockFetchConsent, mockAdLoader, mockInterstitialLoader, false)
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
