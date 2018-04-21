package nl.entreco.dartsscorecard.di.viewmodel.ad

import android.content.Context
import android.content.res.Resources
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.R
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AdModuleTest {

    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockResources: Resources

    @Test
    fun provideInterstialAd() {
        assertNotNull(AdModule().provideInterstialAd(mockContext))
    }

    @Test
    fun provideInterstitialUnitId() {
        whenever(mockResources.getString(R.string.setup_interstitial_unit_id)).thenReturn("interstitial id")
        assertNotNull(AdModule().provideInterstitialUnitId(mockResources))
    }
}