package nl.entreco.libads

import android.content.Context
import android.content.res.Resources
import org.mockito.kotlin.whenever
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AdModuleTest{

    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockResources: Resources

    @Test
    fun provideInterstialAd() {
        assertNotNull(AdModule.provideInterstialAd(mockContext))
    }

    @Test
    fun provideInterstitialUnitId() {
        whenever(mockContext.resources).thenReturn(mockResources)
        whenever(mockResources.getString(R.string.setup_interstitial_unit_id)).thenReturn("interstitial id")
        assertNotNull(AdModule.provideInterstitialUnitId(mockContext))
    }
}