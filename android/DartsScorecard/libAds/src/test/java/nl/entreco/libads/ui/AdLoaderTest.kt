package nl.entreco.libads.ui

import com.google.android.gms.ads.AdView
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.libconsent.fetch.FetchCurrentConsentUsecase
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class AdLoaderTest {

    private val mockView: AdView = mock()
    private val mockFetchCurrentConsentUsecase: FetchCurrentConsentUsecase = mock()
    private val mockListener: AdLoader.AdListener = mock()
    private lateinit var subject: AdLoader

    @Before
    fun setUp() {
        subject = AdLoader(mockFetchCurrentConsentUsecase)
    }

    @Test
    fun onAdLoaded() {
        loadAd()
        subject.onAdLoaded()
        verify(mockListener).onAdLoaded()
    }

    @Test
    fun onAdFailedToLoad() {
        loadAd()
        subject.onAdFailedToLoad(12)
        verify(mockListener).onAdFailed()
    }

    private fun loadAd() {
        subject.loadAd(mockView, mockListener)
    }

}