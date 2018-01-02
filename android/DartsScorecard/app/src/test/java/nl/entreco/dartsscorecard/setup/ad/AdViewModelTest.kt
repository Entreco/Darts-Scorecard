package nl.entreco.dartsscorecard.setup.ad

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.Analytics
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 29/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class AdViewModelTest {

    @Mock private lateinit var mockAnalytics: Analytics
    private lateinit var subject: AdViewModel

    @Before
    fun setUp() {
        givenViewModel()
    }

    @Test
    fun `it should track achievement, when ad is clicked`() {
        whenOnAdClicked()
        thenAchieventIsTracked()
    }

    @Test
    fun onAdLoaded() {
        whenOnAdLoaded()
        thenAdIsShown()
    }

    @Test
    fun onAdFailed() {
        whenOnAdFailed(400)
        thenAdIsHidden()
    }

    private fun givenViewModel() {
        subject = AdViewModel(mockAnalytics)
    }

    private fun whenOnAdClicked() {
        subject.onAdClicked()
    }

    private fun whenOnAdLoaded() {
        subject.onAdLoaded()
    }

    private fun whenOnAdFailed(err:Int) {
        subject.onAdFailed(err)
    }

    private fun thenAchieventIsTracked() {
        verify(mockAnalytics).trackAchievement(any())
    }

    private fun thenAdIsShown() {
        assertTrue(subject.showAd.get())
    }

    private fun thenAdIsHidden() {
        assertFalse(subject.showAd.get())
    }

}
