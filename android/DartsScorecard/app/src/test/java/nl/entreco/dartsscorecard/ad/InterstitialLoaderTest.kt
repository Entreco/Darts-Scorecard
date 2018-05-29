package nl.entreco.dartsscorecard.ad

import android.os.Handler
import com.google.android.gms.ads.InterstitialAd
import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class InterstitialLoaderTest {

    @Mock private lateinit var mockInterstitial: InterstitialAd
    @Mock private lateinit var mockHandler: Handler
    private lateinit var subject: InterstitialLoader

    private var runnableCaptor = argumentCaptor<Runnable>()

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
        subject = InterstitialLoader("interstialAdUnitId", mockHandler, mockInterstitial)
    }

    private fun whenShowingInterstitial() {
        subject.showInterstitial()
        verify(mockHandler).postDelayed(runnableCaptor.capture(), 250)
        runnableCaptor.lastValue.run()
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