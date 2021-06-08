package nl.entreco.libads

import android.content.Context
import android.content.res.Resources
import nl.entreco.libconsent.fetch.FetchConsentUsecase
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AdModuleTest {

    private val mockFetchConsent: FetchConsentUsecase = mock()
    private val mockContext: Context = mock()
    private val mockResources: Resources = mock()

    @Test
    fun provideInterstialAd() {
        assertNotNull(AdModule.provideAds(mockContext, mockFetchConsent))
    }

    @Test
    fun provideInterstitialUnitId() {
        whenever(mockContext.resources).thenReturn(mockResources)
        whenever(mockResources.getString(R.string.setup_interstitial_unit_id)).thenReturn("interstitial id")
        assertNotNull(AdModule.provideInterstials(mockContext))
    }
}