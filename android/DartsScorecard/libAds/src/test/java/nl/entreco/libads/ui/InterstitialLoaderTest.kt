package nl.entreco.libads.ui

import android.os.Handler
import com.google.android.gms.ads.InterstitialAd
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.mockito.Mockito

class InterstitialLoaderTest {

    private val mockInterstitial: InterstitialAd = mock()
    private val mockHandler: Handler = mock()
    private lateinit var subject: InterstitialLoader

    private var runnableCaptor = argumentCaptor<Runnable>()

    @Test
    fun `it should show Interstitial when loaded ok`() {
        givenSubject(true)
        whenShowingInterstitial(true)
        thenInterstialIsShown()
    }

    @Test
    fun `it should NOT show interstitial when not loaded ok`() {
        givenSubject(false)
        whenShowingInterstitial(false)
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

    private fun whenShowingInterstitial(loaded: Boolean) {
        subject.showInterstitial()

        if (loaded) {
            verify(mockHandler).postDelayed(runnableCaptor.capture(), eq(250))
            runnableCaptor.lastValue.run()
        }
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
        verify(mockInterstitial, Mockito.times(2)).loadAd(any())
    }
}