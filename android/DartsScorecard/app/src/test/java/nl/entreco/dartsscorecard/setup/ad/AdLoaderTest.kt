package nl.entreco.dartsscorecard.setup.ad

import com.google.android.gms.ads.AdView
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.ad.AdBindings.Companion.loadAd
import nl.entreco.dartsscorecard.ad.AdLoader
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 29/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class AdLoaderTest {

    @Mock private lateinit var mockView: AdView
    @Mock private lateinit var mockListener: AdLoader.AdListener
    private lateinit var subject: AdLoader

    @Before
    fun setUp() {
        subject = AdLoader("id")
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

    private fun loadAd(){
        subject.loadAd(mockView, mockListener)
    }

}