package nl.entreco.dartsscorecard.beta

import android.text.Html
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.ImageView
import android.widget.TextView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 06/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class BetaBindingsTest {

    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockTextView: TextView
    @Mock private lateinit var mockImageView: ImageView
    @Mock private lateinit var mockAnimator: ViewPropertyAnimator

    @Test(expected = NullPointerException::class)
    fun loadImage() {
        BetaBindings.loadImage(mockImageView, "some url")
        verify(mockView).context
    }

    @Test
    fun `loadImage with null`() {
        BetaBindings.loadImage(mockImageView, null)
        verify(mockView, never()).context
    }

    @Test
    fun animateProgress() {
        setupMocking()
        BetaBindings.animateProgress(mockView, 0.5F, 0.0F)
        verify(mockView).animate()
    }

    @Test
    fun `animateProgress negative`() {
        setupMocking()
        BetaBindings.animateProgress(mockView, 0.5F, 0.8F)
        verify(mockView).scaleX = 0.5F
    }

    @Test
    fun `it should set text with tags`() {
        val message = "Some html message"
        BetaBindings.setTextWithTags(mockTextView, message)
        verify(mockTextView).text = Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
    }

    @Test
    fun `it should do nothing when text is null`() {
        val message = null
        BetaBindings.setTextWithTags(mockTextView, message)
        verify(mockTextView, never()).text = any()
    }

    private fun setupMocking() {
        whenever(mockView.animate()).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setDuration(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setStartDelay(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setInterpolator(any())).thenReturn(mockAnimator)
    }

}
