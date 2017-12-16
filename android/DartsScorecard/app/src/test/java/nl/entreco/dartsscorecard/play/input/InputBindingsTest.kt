package nl.entreco.dartsscorecard.play.input

import android.support.design.widget.FloatingActionButton
import android.view.ViewPropertyAnimator
import android.widget.TextView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.R
import nl.entreco.domain.play.listeners.events.BustEvent
import nl.entreco.domain.play.listeners.events.NoScoreEvent
import nl.entreco.domain.play.listeners.events.ThrownEvent
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 16/12/2017.
 */
class InputBindingsTest {

    @Mock private lateinit var mockTextView: TextView
    @Mock private lateinit var mockFab: FloatingActionButton
    @Mock private lateinit var mockAnimator: ViewPropertyAnimator

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        whenever(mockTextView.animate()).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleY(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setDuration(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.translationX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setStartDelay(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.withEndAction(any())).thenReturn(mockAnimator)
    }

    @Test
    fun showScore() {
        InputBindings.showScore(mockTextView, "25")
        verify(mockTextView).text = "25"
    }

    @Test
    fun backAnimation() {
        InputBindings.backAnimation(mockTextView, "")
        verify(mockTextView).animate()
        verify(mockAnimator).scaleX(0F)
        verify(mockAnimator).start()
    }

    @Test
    fun reverseBackAnimation() {
        InputBindings.backAnimation(mockTextView, "some score input")
        verify(mockTextView).animate()
        verify(mockAnimator).scaleX(1F)
        verify(mockAnimator).start()
    }

    @Test
    fun numDartsState_1() {
        InputBindings.numDartsState(mockFab, 1)
        verify(mockFab).isSelected = false
        verify(mockFab).isActivated = false
    }

    @Test
    fun numDartsState_2() {
        InputBindings.numDartsState(mockFab, 2)
        verify(mockFab).isSelected = false
        verify(mockFab).isActivated = true
    }

    @Test
    fun numDartsState_3() {
        InputBindings.numDartsState(mockFab, 3)
        verify(mockFab).isSelected = true
        verify(mockFab).isActivated = false
    }

    @Test
    fun showSpecialEvents_bust() {
        InputBindings.showSpecialEvents(mockTextView, BustEvent())
        verify(mockTextView).setText(R.string.bust)
    }

    @Test
    fun showSpecialEvents_noscore() {
        InputBindings.showSpecialEvents(mockTextView, NoScoreEvent(true))
        verify(mockTextView).setText(R.string.no_score)
    }

    @Test
    fun showSpecialEvents_throw() {
        InputBindings.showSpecialEvents(mockTextView, ThrownEvent("yeah"))
        verify(mockTextView).text = "yeah"
    }

}