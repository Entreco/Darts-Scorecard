package nl.entreco.dartsscorecard.play.score

import android.widget.TextView
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.widget.CounterTextView
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 28/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class TeamScoreBindingsTest {

    @Mock private lateinit var mockCounterTextView: CounterTextView
    @Mock private lateinit var mockTextView: TextView

    @Test
    @Ignore("unable to mock CounterTextView -> base class is abstract ??")
    fun `it should setTarget() on CounterTextView (ScorePts TextView)`() {
        TeamScoreBindings.showScore(mockCounterTextView, 50)
        verify(mockCounterTextView).setTarget(50)
    }

    @Test
    @Ignore("unable to mock CounterTextView -> base class is abstract ??")
    fun `it should setTarget() on CounterTextView (Hint TextView)`() {
        TeamScoreBindings.showCurrentScore(mockCounterTextView, 501)
        verify(mockCounterTextView).setTarget(501)
    }

    @Test
    fun `it should set 'starter' drawable when player is starting player`() {
        givenPlayerStartedLeg()

        thenStarterDrawableIsShown()
    }

    @Test
    fun `it should clear 'starter' drawable when player is !starting player`() {
        givenPlayerWasNotStarting()
        thenStarterDrawableIsNotShown()
    }

    private fun givenPlayerWasNotStarting() {
        TeamScoreBindings.showStartingPlayer(mockTextView, false)
    }

    private fun givenPlayerStartedLeg() {
        TeamScoreBindings.showStartingPlayer(mockTextView, true)
    }

    private fun thenStarterDrawableIsShown() {
        verify(mockTextView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.starter, 0)
    }

    private fun thenStarterDrawableIsNotShown() {
        verify(mockTextView).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    }

}
