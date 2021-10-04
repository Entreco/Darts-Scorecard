package nl.entreco.libads.ui

import org.mockito.kotlin.mock
import nl.entreco.domain.ad.FetchFinishedGamesUsecase
import nl.entreco.libads.Ads
import nl.entreco.libads.Interstitials
import nl.entreco.libconsent.fetch.FetchConsentUsecase

class AdViewModelTest {

    private val mMockFetchItemsUsecase: FetchFinishedGamesUsecase = mock()
    private val mockFetchConsent: FetchConsentUsecase = mock()
    private val mockAdLoader: Ads = mock()
    private val mockInterstitialLoader: Interstitials = mock()

    private lateinit var subject : AdViewModel
//
//    @Test
//    fun `it should provide ad if not debugging`() {
//        givenSubject(debugging = false)
//        whenProvidingAdd()
//        thenAdIsShown()
//    }
//
//    @Test
//    fun `it should deregister on destroy`() {
//        givenSubject()
//        whenDestroying()
//        thenObserverIsRemoved()
//    }
//
//    @Test
//    fun `it should not serve adds initially`() {
//        givenSubject()
//        whenLoadingAdSucceeds()
//        thenShowAdIs(false)
//    }
//
//    @Test
//    fun `it should NOT serve adds when purchasedItems exist`() {
//        givenSubject()
//        givenPurchasedItems(true)
//        whenLoadingAdSucceeds()
//        thenShowAdIs(false)
//    }
//
//    @Test
//    fun `it should serve adds when no purchasedItems exist`() {
//        givenSubject()
//        givenPurchasedItems(false)
//        whenLoadingAdSucceeds()
//        thenShowAdIs(true)
//    }
//
//    @Test
//    fun `it should not serve adds when loading fails`() {
//        givenSubject()
//        whenLoadingAdFails()
//        thenShowAdIs(false)
//    }
//
//    @Test
//    fun `it should NOT serve interstitial initially`() {
//        givenSubject()
//        whenProvidingInterstitials()
//        thenNoInterstialIsLoaded()
//    }
//
//    @Test
//    fun `it should NOT serve interstitial when unable to bind`() {
//        givenSubject()
//        givenBindingToBillingFails()
//        whenProvidingInterstitials()
//        thenNoInterstialIsLoaded()
//    }
//
//    @Test
//    fun `it should serve interstitial when no items purchased`() {
//        givenSubject()
//        givenPurchasedItems(false)
//        whenProvidingInterstitials()
//        thenInterstialIsLoaded()
//    }
//
//    @Test
//    fun `it should NOT serve interstitial when items purchased`() {
//        givenSubject()
//        givenPurchasedItems(true)
//        whenProvidingInterstitials()
//        thenNoInterstialIsLoaded()
//    }
//
//    @Test
//    fun `it should NOT serve interstitial when items cannot be retrieved`() {
//        givenSubject()
//        givenPurchasedItemsFails()
//        whenProvidingInterstitials()
//        thenNoInterstialIsLoaded()
//    }
//
//    private fun givenSubject(debugging: Boolean = false) {
//        subject = AdViewModel(mockFetchItemsUsecase, mockFetchConsent, mockAdLoader, mockInterstitialLoader, debugging)
//    }
//
//    private fun givenPurchasedItems(purchasedItems: Boolean){
//        val bindCaptor = argumentCaptor<(Boolean)->Unit>()
//        val doneCaptor = argumentCaptor<(FetchPurchasedItemsResponse)->Unit>()
//        subject.onPurchasesRetrieved()
//        bindCaptor.lastValue.invoke(true)
//        verify(mockFetchItemsUsecase).exec(doneCaptor.capture(), any())
//        doneCaptor.lastValue.invoke(FetchPurchasedItemsResponse(!purchasedItems))
//    }
//
//    private fun givenPurchasedItemsFails(){
//        val bindCaptor = argumentCaptor<(Boolean)->Unit>()
//        val failCaptor = argumentCaptor<(Throwable)->Unit>()
//        bindCaptor.lastValue.invoke(true)
//        verify(mockFetchItemsUsecase).exec(any(), failCaptor.capture())
//        failCaptor.lastValue.invoke(RuntimeException("Unable to query purchased items"))
//    }
}
