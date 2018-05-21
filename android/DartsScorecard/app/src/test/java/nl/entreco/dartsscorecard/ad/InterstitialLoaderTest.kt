package nl.entreco.dartsscorecard.ad

import com.google.android.gms.ads.InterstitialAd
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class InterstitialLoaderTest {

    @Mock private lateinit var mockInterstitial: InterstitialAd
    private lateinit var subject : InterstitialLoader

    @Test
    fun `it should show Interstitial when loaded ok`() {
        givenSubject(true)
        whenShowingInterstitial()
        thenInterstialIsShown()
    }

    @Test
    fun `it should NOT show interstitial when not loaded ok`() {
        givenSubject(false)
        whenShowingInterstitial()
        thenInterstialIsNotShown()
    }

    @Test
    fun `it should load new interstitial onAdClosed`() {
        givenSubject(true)
        whenAdClosed()
        thenNewInterstitialIsLoaded()
    }

    private fun givenSubject(loaded: Boolean) {
        whenever(mockInterstitial.isLoaded).thenReturn(loaded)
        subject = InterstitialLoader("interstialAdUnitId", mockInterstitial)
    }

    private fun whenShowingInterstitial() {
        subject.showInterstitial()
    }

    private fun whenAdClosed() {
        subject.onAdClosed()
    }

    private fun thenInterstialIsShown() {
        verify(mockInterstitial).show()
    }

    private fun thenInterstialIsNotShown() {
        verify(mockInterstitial, never()).show()
    }

    private fun thenNewInterstitialIsLoaded() {
        verify(mockInterstitial, times(2)).loadAd(any())
    }
}