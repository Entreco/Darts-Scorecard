package nl.entreco.dartsscorecard.di.viewmodel.ad

import android.content.Context
import android.content.res.Resources
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
        assertNotNull(AdModule().provideInterstitialUnitId(mockResources))
    }
}