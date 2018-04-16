package nl.entreco.dartsscorecard.setup.ad

import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.ad.AdLoader
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

    @Mock private lateinit var mockListener: AdLoader.Listener
    @InjectMocks private lateinit var subject: AdLoader

    @Test
    fun onAdClicked() {
        subject.onAdClicked()
        verify(mockListener).onAdClicked()
    }

    @Test
    fun onAdLoaded() {
        subject.onAdLoaded()
        verify(mockListener).onAdLoaded()
    }

    @Test
    fun onAdFailedToLoad() {
        subject.onAdFailedToLoad(12)
        verify(mockListener).onAdFailed(12)
    }

}